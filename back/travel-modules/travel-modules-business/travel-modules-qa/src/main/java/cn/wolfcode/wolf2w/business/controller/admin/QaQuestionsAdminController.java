package cn.wolfcode.wolf2w.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import cn.wolfcode.wolf2w.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;
import cn.wolfcode.wolf2w.business.query.QaQuestionsQuery;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import cn.wolfcode.wolf2w.common.log.annotation.Log;
import cn.wolfcode.wolf2w.common.log.enums.BusinessType;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresPermissions;
import cn.wolfcode.wolf2w.business.api.domain.QaQuestions;
import cn.wolfcode.wolf2w.business.service.IQaQuestionsService;
import cn.wolfcode.wolf2w.common.core.web.controller.BaseController;
import cn.wolfcode.wolf2w.common.core.utils.poi.ExcelUtil;


/**
 * 问答提问Controller
 * 
 * @author yang
 * @date 2025-07-24
 */
@RestController
@RequestMapping("/admin/questionss")
public class QaQuestionsAdminController extends BaseController
{
    @Autowired
    private IQaQuestionsService qaQuestionsService;

    /**
     * 查询问答提问列表
     */
    @RequiresPermissions("business:questions:list")
    @GetMapping("/list")
    public R<IPage<QaQuestions>> list(QaQuestionsQuery qo) {
        return R.ok(qaQuestionsService.queryPage(qo));
    }


    /**
     * 导出问答提问列表
     */
    @RequiresPermissions("business:questions:export")
    @Log(title = "问答提问", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, QaQuestions qaQuestions)
    {
        List<QaQuestions> list = qaQuestionsService.list();
        ExcelUtil<QaQuestions> util = new ExcelUtil<QaQuestions>(QaQuestions.class);
        util.exportExcel(response, list, "问答提问数据");
    }

    /**
     * 获取问答提问详细信息
     */
    @RequiresPermissions("business:questions:query")
    @GetMapping(value = "/{id}")
    public R<QaQuestions> getInfo(@PathVariable("id") Long id)
    {
        return R.ok(qaQuestionsService.getById(id));
    }

    /**
     * 新增问答提问
     */
    @RequiresPermissions("business:questions:add")
    @Log(title = "问答提问", businessType = BusinessType.INSERT)
    @PostMapping
    public R<?> add(@RequestBody QaQuestions qaQuestions)
    {
        return toAjax(qaQuestionsService.save(qaQuestions));
    }

    /**
     * 修改问答提问
     */
    @RequiresPermissions("business:questions:edit")
    @Log(title = "问答提问", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<?> edit(@RequestBody QaQuestions qaQuestions)
    {
        return toAjax(qaQuestionsService.updateById(qaQuestions));
    }

    /**
     * 删除问答提问
     */
    @RequiresPermissions("business:questions:remove")
    @Log(title = "问答提问", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<?> remove(@PathVariable Long[] ids)
    {
        return toAjax(qaQuestionsService.removeByIds(Arrays.stream(ids).collect(Collectors.toList())));
    }
}
