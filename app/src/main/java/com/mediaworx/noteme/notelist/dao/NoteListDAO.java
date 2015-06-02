package com.mediaworx.noteme.notelist.dao;

import com.mediaworx.noteme.common.storage.GenericDAO;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Data access interface for {@link com.mediaworx.noteme.notelist.model.NoteList} objects.
 *
 * @author mko
 *
 */
public interface NoteListDAO extends GenericDAO<Long, NoteList> {

    int updateLight(NoteList noteList);
}
