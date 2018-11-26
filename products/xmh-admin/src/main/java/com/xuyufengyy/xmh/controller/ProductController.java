package com.xuyufengyy.xmh.controller;

import com.xuyufengyy.xmh.*;
import com.xuyufengyy.xmh.baidu.Voice;
import com.xuyufengyy.xmh.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * controller - 景点
 *
 * @author Xu minghua 2018/05/21
 */
@Controller
@RequestMapping(value = "/product")
public class ProductController extends BaseController {

    @Resource
    private ProductService productService;

    @Resource
    private ScenicSpotService scenicSpotService;

    @Resource
    private Voice voice;

    @Resource
    private AdminService adminService;

    @Value(value = "${image.view}")
    private String imageView;

    @Value(value = "${voice.view}")
    private String voiceView;

    /**
     * 列表
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(ModelMap model, Pageable pageable) {
        model.addAttribute("page", productService.findPage(pageable));
        return "product/list";
    }

    /**
     * 检查名称是否存在
     */
    @RequestMapping(value = "/check_name", method = RequestMethod.GET)
    public @ResponseBody
    boolean checkName(Long id, String name) {
        if (StringUtils.isBlank(name)) {
            return false;
        }
        if(id != null){
            Product product = productService.getOne(id);
            if(product != null){
                if(name.equals(product.getName())){
                   return true;
                }
            }
        }
        if (productService.nameExists(name)) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * 添加
     */
    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String add() {
        return "product/add";
    }

    /**
     * 保存
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String save(Product product, Long scenicSpotId, RedirectAttributes redirectAttributes) {
        product.setScenicSpot(scenicSpotService.getOne(scenicSpotId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser") && authentication.isAuthenticated()) {
            Admin admin = (Admin) authentication.getPrincipal();
            product.setAuthor(admin.getNickname());
        }
        if (!isValid(product, BaseEntity.Save.class)) {
            return ERROR_VIEW;
        }

        //生成语音文件
        String fileName = DateUtils.toString(new Date(), "yyyyMMddHHmmss") + (int) ((Math.random() * 9 + 1) * 100000) + ".mp3";
        fileName = voice.synthesis(product.getUploadText(), fileName);
        product.setUploadTape(fileName);
        productService.save(product);

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 编辑
     */
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.GET)
    public String add(@PathVariable("id") Long id, Model model) {
        Product product = productService.getOne(id);
        Area area = product.getScenicSpot().getArea();

        model.addAttribute("product", productService.getOne(id));
        model.addAttribute("scenicSpots", scenicSpotService.findList(area.getId()));
        return "product/edit";
    }

    /**
     * 更新
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(Product product, Long scenicSpotId, RedirectAttributes redirectAttributes) {
        product.setScenicSpot(scenicSpotService.getOne(scenicSpotId));
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getPrincipal().equals("anonymousUser") && authentication.isAuthenticated()) {
            Admin admin = (Admin) authentication.getPrincipal();
            product.setAuthor(admin.getNickname());
        }

        if (!isValid(product)) {
            return ERROR_VIEW;
        }

        //生成语音文件
        String fileName = DateUtils.toString(new Date(), "yyyyMMddHHmmss") + (int) ((Math.random() * 9 + 1) * 100000) + ".mp3";
        fileName = voice.synthesis(product.getUploadText(), fileName);
        product.setUploadTape(fileName);

        if (StringUtils.isBlank(product.getImage())) {
            productService.update(product, "image");
        } else {
            productService.update(product, null);
        }

        addFlashMessage(redirectAttributes, SUCCESS_MESSAGE);
        return "redirect:list";
    }

    /**
     * 查询景区
     *
     * @param areaId
     * @return
     */
    @RequestMapping(value = "/query_scenic_spot", method = RequestMethod.POST)
    public @ResponseBody
    Map<Long, String> queryScenicSpot(Long areaId) {
        Map<Long, String> options = new HashMap<Long, String>();

        List<ScenicSpot> scenicSpots = scenicSpotService.findList(areaId);
        for (ScenicSpot scenicSpot : scenicSpots) {
            options.put(scenicSpot.getId(), scenicSpot.getName());
        }
        return options;
    }

    /**
     * 查看
     */
    @RequestMapping(value = "/view/{id}", method = RequestMethod.GET)
    public String view(@PathVariable("id") Long id, Model model) {
        Product product = productService.getOne(id);

        model.addAttribute("product", productService.getOne(id));
        model.addAttribute("imageView", imageView);
        model.addAttribute("voiceView", voiceView);
        return "product/view";
    }
}
