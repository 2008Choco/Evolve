package wtf.choco.evolve.event;

public interface Cancellable {

    public void setCancelled(boolean cancelled);

    public boolean isCancelled();

}
