package structures;

public abstract class Key<K extends Comparable<K>, D> implements Comparable<Key<K, D>>{

    private D data;

    public Key(D data) {
        this.data = data;
    }

    protected D getData() {
        return data;
    }

    protected abstract K getKey();

    @Override
    public final int compareTo(Key<K, D> keyData) {
        return this.getKey().compareTo(keyData.getKey());
    }
}
