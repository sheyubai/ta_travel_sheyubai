package cn.wolfcode.wolf2w.business.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import cn.wolfcode.wolf2w.business.api.domain.Answer;
import cn.wolfcode.wolf2w.business.query.AnswerQuery;
import cn.wolfcode.wolf2w.business.service.IAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import cn.wolfcode.wolf2w.common.core.domain.R;
import com.baomidou.mybatisplus.core.metadata.IPage;

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

import cn.wolfcode.wolf2w.common.core.web.controller.BaseController;
import cn.wolfcode.wolf2w.common.core.utils.poi.ExcelUtil;


/**
 * 问题回答Controller
 * 
 * @author yang
 * @date 2025-08-12
 */
@RestController
@RequestMapping("/admin/answers")
public class AnswerAdminController extends BaseController
{
    @Autowired
    private IAnswerService answerService;

    /**
     * 查询问题回答列表
     */
    @RequiresPermissions("bussines:answer:list")
    @GetMapping("/list")
    public R<IPage<Answer>> list(AnswerQuery qo) {
        return R.ok(answerService.queryPage(qo));
    }


    /**
     * 导出问题回答列表
     */
    @RequiresPermissions("bussines:answer:export")
    @Log(title = "问题回答", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, Answer answer)
    {
        List<Answer> list = answerService.list();
        ExcelUtil<Answer> util = new ExcelUtil<Answer>(Answer.class);
        util.exportExcel(response, list, "问题回答数据");
    }

    /**
     * 获取问题回答详细信息
     */
    @RequiresPermissions("bussines:answer:query")
    @GetMapping(value = "/{id}")
    public R<Answer> getInfo(@PathVariable("id") Long id)
    {
        return R.ok(answerService.getById(id));
    }

    /**
     * 新增问题回答
     */
    @RequiresPermissions("bussines:answer:add")
    @Log(title = "问题回答", businessType = BusinessType.INSERT)
    @PostMapping
    public R<?> add(@RequestBody Answer answer)
    {
        return toAjax(answerService.save(answer));
    }

    /**
     * 修改问题回答
     */
    @RequiresPermissions("bussines:answer:edit")
    @Log(title = "问题回答", businessType = BusinessType.UPDATE)
    @PutMapping
    public R<?> edit(@RequestBody Answer answer)
    {
        return toAjax(answerService.updateById(answer));
    }

    /**
     * 删除问题回答
     */
    @RequiresPermissions("bussines:answer:remove")
    @Log(title = "问题回答", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public R<?> remove(@PathVariable Long[] ids)
    {
        return toAjax(answerService.removeByIds(Arrays.stream(ids).collect(Collectors.toList())));
    }
}
