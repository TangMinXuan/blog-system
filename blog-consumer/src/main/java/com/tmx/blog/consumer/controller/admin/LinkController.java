package com.tmx.blog.consumer.controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tmx.blog.api.entity.BlogLink;
import com.tmx.blog.api.service.LinkService;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.Result;
import com.tmx.blog.api.util.ResultGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
public class LinkController {

    @Reference
    private LinkService linkService;

    @GetMapping("/links")
    public java.lang.String linkPage(HttpServletRequest request) {
        request.setAttribute("path", "links");
        return "admin/link";
    }

    @GetMapping("/links/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, String> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int page = Integer.parseInt(params.get("page"));
        int limit = Integer.parseInt(params.get("limit"));
        DubboPageUtil dubboPageUtil = new DubboPageUtil(page, limit);
        return ResultGenerator.genSuccessResult(linkService.getBlogLinkPage(dubboPageUtil));
    }

    /**
     * 友链添加
     */
    @RequestMapping(value = "/links/save", method = RequestMethod.POST)
    @ResponseBody
    public Result save(@RequestParam("linkType") Integer linkType,
                       @RequestParam("linkName") java.lang.String linkName,
                       @RequestParam("linkUrl") java.lang.String linkUrl,
                       @RequestParam("linkRank") Integer linkRank,
                       @RequestParam("linkDescription") java.lang.String linkDescription) {
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        BlogLink link = new BlogLink();
        link.setLinkType(linkType.byteValue());
        link.setLinkRank(linkRank);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.saveLink(link));
    }

    /**
     * 详情
     */
    @GetMapping("/links/info/{id}")
    @ResponseBody
    public Result info(@PathVariable("id") Integer id) {
        BlogLink link = linkService.selectById(id);
        return ResultGenerator.genSuccessResult(link);
    }

    /**
     * 友链修改
     */
    @RequestMapping(value = "/links/update", method = RequestMethod.POST)
    @ResponseBody
    public Result update(@RequestParam("linkId") Integer linkId,
                       @RequestParam("linkType") Integer linkType,
                       @RequestParam("linkName") java.lang.String linkName,
                       @RequestParam("linkUrl") java.lang.String linkUrl,
                       @RequestParam("linkRank") Integer linkRank,
                       @RequestParam("linkDescription") java.lang.String linkDescription) {
        BlogLink tempLink = linkService.selectById(linkId);
        if (tempLink == null) {
            return ResultGenerator.genFailResult("无数据！");
        }
        if (linkType == null || linkType < 0 || linkRank == null || linkRank < 0 || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkName) || StringUtils.isEmpty(linkUrl) || StringUtils.isEmpty(linkDescription)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        tempLink.setLinkType(linkType.byteValue());
        tempLink.setLinkRank(linkRank);
        tempLink.setLinkName(linkName);
        tempLink.setLinkUrl(linkUrl);
        tempLink.setLinkDescription(linkDescription);
        return ResultGenerator.genSuccessResult(linkService.updateLink(tempLink));
    }

    /**
     * 友链删除
     */
    @RequestMapping(value = "/links/delete", method = RequestMethod.POST)
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (linkService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

}
