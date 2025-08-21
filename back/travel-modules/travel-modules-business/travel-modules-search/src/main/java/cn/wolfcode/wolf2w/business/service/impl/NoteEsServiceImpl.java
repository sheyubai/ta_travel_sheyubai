package cn.wolfcode.wolf2w.business.service.impl;

import cn.wolfcode.wolf2w.business.api.RemoteNoteService;
import cn.wolfcode.wolf2w.business.api.domain.Note;
import cn.wolfcode.wolf2w.business.api.domain.NoteEs;
import cn.wolfcode.wolf2w.business.repository.NoteEsRepository;
import cn.wolfcode.wolf2w.business.service.INoteEsService;
import cn.wolfcode.wolf2w.common.core.constant.SecurityConstants;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NoteEsServiceImpl implements INoteEsService {
    @Autowired
    private NoteEsRepository repository;
    @Autowired
    private RemoteNoteService remoteNoteService;



    @Override
    public void initData() {
        List<Note> list = remoteNoteService.list(SecurityConstants.INNER).getData();
        List<NoteEs>eslist=new ArrayList<>();
        for (Note note : list) {
            NoteEs es = new NoteEs();
            BeanUtils.copyProperties(note, es);
            eslist.add(es);
        }
        repository.saveAll(eslist);
    }
}
