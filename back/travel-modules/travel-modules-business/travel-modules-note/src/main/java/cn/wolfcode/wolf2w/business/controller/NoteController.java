package cn.wolfcode.wolf2w.business.controller;

import cn.wolfcode.wolf2w.common.core.domain.R;
import cn.wolfcode.wolf2w.common.security.annotation.InnerAuth;
import cn.wolfcode.wolf2w.business.api.domain.Note;
import cn.wolfcode.wolf2w.business.query.NoteQuery;
import cn.wolfcode.wolf2w.business.service.INoteService;
import cn.wolfcode.wolf2w.common.security.annotation.RequiresLogin;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 旅游日记 Controller
 *
 * @author yang
 * @date 2025-07-24
 */
@RestController
@RequestMapping("notes")
public class NoteController {
    @Autowired
    private INoteService noteService;


    @RequiresLogin
    @PostMapping("/add")
    public R<?> add(@RequestBody Note note) {
        noteService.savenote(note);
        return R.ok();
    }

    @GetMapping("/viewnnumTop3")
    public R<?> viewNumTop3(@RequestParam("destId") Long destId) {
        List<Note> list=noteService.queryviewnumTop3(destId);
        return R.ok(list);
    }

    /**
     * 旅游日记详情
     */
    @GetMapping("/detail/{id}")
    public R<Note> detail(@PathVariable("id") Long id) {
        Note note = noteService.queryById(id);
        return R.ok(note);
    }

    /**
     * 旅游日记 列表
     */
    @GetMapping("/query")
    public R<IPage<Note>> query(NoteQuery qo) {
        IPage<Note> page = noteService.queryPage(qo);
        return R.ok(page);
    }


    /*****************************************对外暴露Fegin接口**********************************************/
    /**
     * Feign 接口
     */
    @GetMapping("/feign/list")
    public R<List<Note>> feignList() {
        return R.ok(noteService.list());
    }

    @InnerAuth
    @GetMapping("/feign/{id}")
    public R<Note> feignGet(@PathVariable Long id) {
        return R.ok(noteService.getById(id));
    }
}
