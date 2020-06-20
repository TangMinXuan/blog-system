package com.tmx.blog.provider.serviceImpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.tmx.blog.api.entity.BlogLink;
import com.tmx.blog.api.service.LinkService;
import com.tmx.blog.api.util.DubboPageUtil;
import com.tmx.blog.api.util.PageQueryUtil;
import com.tmx.blog.api.util.PageResult;
import com.tmx.blog.provider.dao.BlogLinkMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {

    @Autowired
    private BlogLinkMapper blogLinkMapper;

    /**
     * 获取 Link 的分页数据
     */
    @Override
    public PageResult getBlogLinkPage(DubboPageUtil dubboPageUtil) {
        PageQueryUtil pageUtil = new PageQueryUtil(dubboPageUtil);
        List<BlogLink> links = blogLinkMapper.findLinkList(pageUtil);
        int total = blogLinkMapper.getTotalLinks();
        PageResult pageResult = new PageResult(links, total, pageUtil.getLimit(), pageUtil.getPage());
        return pageResult;
    }

    /**
     * 获取 Link 总数
     */
    @Override
    public int getTotalLinks() {
        return blogLinkMapper.getTotalLinks();
    }

    /**
     * 插入一条新 Link
     */
    @Override
    public Boolean saveLink(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }

    @Override
    public Map<Byte, List<BlogLink>> getLinksForLinkPage() {
        //获取所有链接数据
        List<BlogLink> links = blogLinkMapper.findLinkList(null);
        if (!CollectionUtils.isEmpty(links)) {
            //根据type进行分组
            Map<Byte, List<BlogLink>> linksMap = links.stream().collect(Collectors.groupingBy(BlogLink::getLinkType));
            return linksMap;
        }
        return null;
    }
}
