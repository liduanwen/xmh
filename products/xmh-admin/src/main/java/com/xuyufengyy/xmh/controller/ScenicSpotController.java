package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.AreaService;
import com.xuyufengyy.xmh.BaseEntity.Save;
import com.xuyufengyy.xmh.ScenicSpot;
import com.xuyufengyy.xmh.ScenicSpotService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;

/**
 * controller - 景区
 *
 * @author Xu minghua 2018/05/24
 */
@Controller
@RequestMapping(value = "/scenic_spot")
public class ScenicSpotController extends BaseController {

    @Resource
    private AreaService areaService;

    @Resource
    private ScenicSpotService scenicSpotService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model, Pageable pageable) {
        model.addAttribute("page", scenicSpotService.findPage(pageable));
        return "scenic_spot/list";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "scenic_spot/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(ScenicSpot scenicSpot, Long areaId, RedirectAttributes redirectAttributes) {
        if (areaId != null) {
            scenicSpot.setArea(areaService.getOne(areaId));
        }
        if (!isValid(scenicSpot, Save.class)) {
            return ERROR_VIEW;
        }

        scenicSpotService.save(scenicSpot);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String add(@PathVariable("id") Long id, Model model) {
        model.addAttribute("scenicSpot", scenicSpotService.getOne(id));
        return "scenic_spot/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(ScenicSpot scenicSpot, Long areaId, RedirectAttributes redirectAttributes) {
        if (areaId != null) {
            scenicSpot.setArea(areaService.getOne(areaId));
        }
        if (!isValid(scenicSpot)) {
            return ERROR_VIEW;
        }

        if (StringUtils.isBlank(scenicSpot.getImage())) {
            scenicSpotService.update(scenicSpot, "image");
        } else {
            scenicSpotService.update(scenicSpot, null);
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }
}
