package com.mediaworx.noteme.notelist.dao;

import com.mediaworx.noteme.common.storage.GenericDAO;
import com.mediaworx.noteme.notelist.model.Note;
import com.mediaworx.noteme.notelist.model.NoteList;

import java.util.List;

/**
 * Data access interface for {@link com.mediaworx.noteme.notelist.model.Note} objects.
 *
 * @author mko
 *
 */
public interface NoteDAO extends GenericDAO<Long, Note> {

    List<Note> readAllNotes(Long noteListId);


    List<Long> readAllNoteIds(Long noteListId);
}
