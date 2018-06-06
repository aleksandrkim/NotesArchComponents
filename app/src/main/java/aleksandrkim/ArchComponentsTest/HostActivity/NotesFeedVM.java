package aleksandrkim.ArchComponentsTest.HostActivity;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;
import android.os.AsyncTask;
import android.util.Pair;

import aleksandrkim.ArchComponentsTest.Db.AppDatabase;
import aleksandrkim.ArchComponentsTest.Db.Note;
import aleksandrkim.ArchComponentsTest.Utils.Colors;
import aleksandrkim.ArchComponentsTest.Utils.Event;

/**
 * Created by Aleksandr Kim on 11 Apr, 2018 11:37 PM for ArchComponentsTest
 */

public class NotesFeedVM extends AndroidViewModel {

    private final String TAG = "NotesFeedVM";

    private AppDatabase db;

    private LiveData<PagedList<Note>> pagedNotes;
    private MutableLiveData<Event<Pair<Integer, Integer>>> swipedNote;

    public NotesFeedVM(Application application) {
        super(application);
        db = AppDatabase.getDb(this.getApplication());
        swipedNote = new MutableLiveData<>();
    }

    public void subscribeToPagedNotes(int pageSize) {
        pagedNotes = new LivePagedListBuilder<>(db.noteRoomDao().getNotesPagedLastCreatedFirst(), pageSize).build();
    }

    public void addSampleNotes(final int count) {
        AsyncTask.execute(() -> {
            for (int i = 0; i < count; i++) {
                Note note = new Note("Sample", "Content content content content content content content content content content content content content content content content content content content content content", Colors.colors[i % Colors.colors.length]);
                db.noteRoomDao().add(note);
            }
        });
    }

    public void removeAllObs(LifecycleOwner owner) {
        getAllPagedNotes().removeObservers(owner);
        getSwipedNote().removeObservers(owner);
    }

    public void deleteNote(final int id) {
        AsyncTask.execute(() -> db.noteRoomDao().delete(id));
    }

    public void deleteAllNotes() {
        AsyncTask.execute(() -> db.noteRoomDao().deleteAll());
    }

    public LiveData<PagedList<Note>> getAllPagedNotes() {
        return pagedNotes;
    }

    public void swipe(int noteId, int swipePosition) {
        swipedNote.setValue(new Event<>(Pair.create(noteId, swipePosition)));
    }

    public LiveData<Event<Pair<Integer, Integer>>> getSwipedNote() {
        return swipedNote;
    }
}
