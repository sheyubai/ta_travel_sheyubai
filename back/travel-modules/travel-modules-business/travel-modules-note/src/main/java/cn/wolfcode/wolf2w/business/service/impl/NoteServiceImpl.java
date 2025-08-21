package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteDestinationService;
import cn.wolfcode.wolf2w.business.api.domain.Destination;
import cn.wolfcode.wolf2w.business.api.domain.Note;
import cn.wolfcode.wolf2w.business.api.domain.NoteContent;
import cn.wolfcode.wolf2w.business.mapper.NoteContentMapper;
import cn.wolfcode.wolf2w.business.mapper.NoteMapper;
import cn.wolfcode.wolf2w.business.query.NoteCondition;
import cn.wolfcode.wolf2w.business.query.NoteQuery;
import cn.wolfcode.wolf2w.business.service.INoteService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import cn.wolfcode.wolf2w.common.core.context.SecurityContextHolder;
import cn.wolfcode.wolf2w.member.api.RemoteUserInfoService;
import cn.wolfcode.wolf2w.member.api.domain.UserInfo;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * 旅游日记Service业务层处理
 *
 * @author yang
 * @date 2025-07-24
 */
@Service
@Transactional
public class NoteServiceImpl extends ServiceImpl<NoteMapper, Note> implements INoteService {


    @Autowired
    private RemoteUserInfoService remoteUserInfoService;

    @Autowired
    private NoteContentMapper noteContentMapper;

    @Autowired
    private RemoteDestinationService remoteDestinationService;

    @Override
    public IPage<Note> queryPage(NoteQuery qo) {
        IPage<Note> page = new Page<>(qo.getCurrentPage(), qo.getPageSize());
        QueryWrapper<Note> qw = new QueryWrapper<>();
        qw.orderByDesc(qo.getOrderBy());
        NoteCondition daycondition = NoteCondition.DAY_MAP.get(qo.getDayType());

        if (daycondition != null) {
            qw.between("days", daycondition.getMin(), daycondition.getMax());
        }
        NoteCondition consumeCondition = NoteCondition.CONSUME_MAP.get(qo.getConsumeType());
        if (consumeCondition != null) {
            qw.between("avg_consume", consumeCondition.getMin(), consumeCondition.getMax());
        }
        NoteCondition travelTimeCondition = NoteCondition.TIME_MAP.get(qo.getTravelTimeType());
        if (travelTimeCondition != null) {
            qw.between("MONTH(travel_time)", travelTimeCondition.getMin(), travelTimeCondition.getMax());
        }

        baseMapper.selectPage(page, qw);
        for (Note note : page.getRecords()) {
            Long authorId = note.getAuthorId();
            UserInfo author = remoteUserInfoService.getOne(authorId, SecurityConstants.INNER).getData();
            note.setAuthor(author);
        }
        return page;
    }

    @Override
    public Note queryById(Long id) {
        Note note = baseMapper.selectById(id);
        Long authorId = note.getAuthorId();
        UserInfo author = remoteUserInfoService.getOne(authorId, SecurityConstants.INNER).getData();
        note.setAuthor(author);

        NoteContent noteContent = noteContentMapper.selectById(id);
        note.setContent(noteContent);
        return note;
    }

    @Override
    public List<Note> queryviewnumTop3(Long destId) {
        return lambdaQuery().eq(Note::getDestId, destId)
                .orderByDesc(Note::getViewnum)
                .last("limit 3")
                .list();
    }

    @Override
    public void savenote(Note note) {
        Destination destination = remoteDestinationService.getOne(note.getDestId(), SecurityConstants.INNER).getData();
                note.setDestName(destination.getName());
        Long userId = SecurityContextHolder.getUserId();
        note.setAuthorId(userId);
        Date now = new Date();
        note.setCreateTime(now);
        note.setUpdateTime(now);
        note.setStatus("0");
        note.setReplynum(0L);
        note.setViewnum(0L);
        note.setFavornum(0L);
        note.setSharenum(0L);
        note.setThumbsupnum(0L);
        baseMapper.insert(note);
        Long id = note.getId();
        String content = note.getContent().getContent();
        NoteContent noteContent = new NoteContent();
        noteContent.setId(id);
        noteContent.setContent(content);
        noteContentMapper.insert(noteContent);
    }
}
