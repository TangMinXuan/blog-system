package com.tmx.blog.consumer.controller.admin;

import com.alibaba.dubbo.config.annotation.Reference;
import com.tmx.blog.api.service.CommentService;
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
public class CommentController {

    @Reference
    private CommentService commentService;

    @GetMapping("/comments/list")
    @ResponseBody
    public Result list(@RequestParam Map<String, String> params) {
        if (StringUtils.isEmpty(params.get("page")) || StringUtils.isEmpty(params.get("limit"))) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        int page = Integer.parseInt(params.get("page"));
        int limit = Integer.parseInt(params.get("limit"));
        DubboPageUtil dubboPageUtil = new DubboPageUtil(page, limit);
        return ResultGenerator.genSuccessResult(commentService.getCommentsPage(dubboPageUtil));
    }

    @PostMapping("/comments/checkDone")
    @ResponseBody
    public Result checkDone(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.checkDone(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("审核失败");
        }
    }

    @PostMapping("/comments/reply")
    @ResponseBody
    public Result checkDone(@RequestParam("commentId") Long commentId,
                            @RequestParam("replyBody") java.lang.String replyBody) {
        if (commentId == null || commentId < 1 || StringUtils.isEmpty(replyBody)) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.reply(commentId, replyBody)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("回复失败");
        }
    }

    @PostMapping("/comments/delete")
    @ResponseBody
    public Result delete(@RequestBody Integer[] ids) {
        if (ids.length < 1) {
            return ResultGenerator.genFailResult("参数异常！");
        }
        if (commentService.deleteBatch(ids)) {
            return ResultGenerator.genSuccessResult();
        } else {
            return ResultGenerator.genFailResult("刪除失败");
        }
    }

    @GetMapping("/comments")
    public java.lang.String list(HttpServletRequest request) {
        request.setAttribute("path", "comments");
        return "admin/comment";
    }


}
