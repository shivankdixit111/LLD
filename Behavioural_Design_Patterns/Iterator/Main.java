package Low_Level_Design.Behavioural_Design_Patterns.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

interface Iterator {
    boolean hasNext();
    String next();
}

class SimplePlayListIterator implements Iterator {
    private PlayList playlist;
    private int index;

    SimplePlayListIterator(PlayList playlist, int index) {
        this.playlist = playlist;
        this.index = index;
    }

    @Override
    public boolean hasNext() {
        return index < playlist.getSongs().size();
    }

    public String next() {
        return playlist.getSongs().get(index++);
    }
}

class ShuffledPlayListIterator implements Iterator {
    private PlayList playlist;
    private int index;
    private List<String> shuffledSongs;

    ShuffledPlayListIterator(PlayList playlist, int index) {
        this.playlist = playlist;
        this.index = index;
        shuffledSongs = playlist.getSongs();

        Collections.shuffle(shuffledSongs);
    }

    @Override
    public boolean hasNext() {
        return index < shuffledSongs.size();
    }

    public String next() {
        return shuffledSongs.get(index++);
    }
}

class PlayList {
    private List<String> songs;

    PlayList() {
        songs = new ArrayList<>();
    }
    
    public void addSongs(String song) {
        songs.add(song);
    }

    public Iterator iterator(String type) {
        if(type.equalsIgnoreCase("Simple")) {
            return new SimplePlayListIterator(this, 0); 
        } else if(type.equalsIgnoreCase("shuffled")) {
            return new ShuffledPlayListIterator(this, 0);
        }
        return null;
    }

    public List<String> getSongs() {
        return songs;
    }
}


public class Main {
    public static void main(String[] args) {
        PlayList p = new PlayList();
        p.addSongs("ghar more pardeshiya");
        p.addSongs("bulleya Fav");
        p.addSongs("kar har maidan fateh");
        p.addSongs("zinda Fav");

        Iterator it = p.iterator("simple");
        System.out.println("Playing simple music playlist : ");
        while(it.hasNext()) {
            System.out.println(it.next() );
        }
        System.out.println();

        it = p.iterator("shuffled");
        System.out.println("Playing shuffled music playlist : ");
        while(it.hasNext()) {
            System.out.println(it.next());
        }
        System.out.println();
    }
}
