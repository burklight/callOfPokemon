package utilities.communications;

public interface Communications {

    public void send(DataPacket o) throws Exception;

    public DataPacket receive() throws Exception;

    public void close() throws Exception;

    public void waitConnection();

    public boolean isWorking();

    public void setWorking(boolean b);

    public void setCharacter(String characterName);

    public int getNumPlayer();

    public void setNumPlayer(int numPlayer);
}
