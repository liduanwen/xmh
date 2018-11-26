package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.Area;
import com.xuyufengyy.xmh.AreaService;
import com.xuyufengyy.xmh.BaseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * controller - 地区
 *
 * @author Xu minghua 2018/05/21
 */
@Controller
@RequestMapping(value = "/area")
public class AreaController extends BaseController {

    @Resource
    private AreaService areaService;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Long parentId, ModelMap model) {
        Area parent = null;
        List<Area> areas = new ArrayList<Area>();

        if (parentId != null) {
            parent = areaService.getOne(parentId);
            areas = parent.getChildren();
        } else {
            areas = areaService.findRoots();
        }

        List list = new ArrayList();
        int j = 1;
        for (int i = 0; i < areas.size(); i++) {
            if (areas.size() >= j * 5 && i % 5 == 0) {//5个一组
                list.add(areas.subList(i, j * 5));
                j++;
            } else if (areas.size() < j * 5) {
                list.add(areas.subList((j - 1) * 5, areas.size()));
                break;
            }
        }

        model.addAttribute("parent", parent);
        model.addAttribute("areas", list);
        return "area/list";
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add(Long parentId, ModelMap model) {
        if (parentId != null) {
            model.addAttribute("parent", areaService.getOne(parentId));
        }
        return "area/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Area area, Long parentId, RedirectAttributes redirectAttributes) {
        if (parentId != null) {
            area.setParent(areaService.getOne(parentId));
        }
        if (!isValid(area, BaseEntity.Save.class)) {
            return ERROR_VIEW;
        }
        area.setFullName(null);
        area.setTreePath(null);
        area.setChildren(null);
        areaService.save(area);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String add(@PathVariable("id") Long id, Model model) {
        model.addAttribute("area", areaService.getOne(id));
        return "area/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Area area, RedirectAttributes redirectAttributes) {
        if (!isValid(area)) {
            return ERROR_VIEW;
        }
        areaService.update(area, "fullName", "treePath", "parent", "children", "members", "receivers", "orders", "deliveryCenters");
        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }
}
