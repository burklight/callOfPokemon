package utilities.communications;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * This class controls the NIO connection of the client.
 */
public class ClientNIO implements Communications {

    protected SocketChannel sc;
    protected Converter conv;
    protected Object objAPasar = null;
    protected Object objRecibido = null;
    protected String character;
    protected volatile boolean working;
    protected int numPlayer;

    public ClientNIO() {
        try {
            working = true;
            character = "";
            conv = new Converter();
            sc = SocketChannel.open(new InetSocketAddress(Comms.serverPort));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Converts the DataPacket to byteBuffer and sends it through the
     * SocketChannel.
     *
     * @param o
     * @throws Exception
     */
    @Override
    public void send(DataPacket o) throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(Comms.SIZE_BUFFER);
        ByteBuffer b = conv.ObtectToByteBuffer(o);
        bb.put(b.array());
        bb.flip();
        sc.write(bb);
        bb.clear();
    }

    /**
     * Reads a byteBuffer from the SocketChannel and converts it to DataPacket.
     *
     * @return The DataPacket
     * @throws Exception
     */
    @Override
    public DataPacket receive() throws Exception {
        ByteBuffer bb = ByteBuffer.allocate(Comms.SIZE_BUFFER);
        sc.read(bb);
        bb.flip();
        Object obj = conv.ByteBufferToObject(bb);
        return (DataPacket) obj;
    }

    /**
     * Waits for a connection.
     */
    @Override
    public void waitConnection() {
        try {

            System.out.println("Waiting for accepted connection...");
            DataPacket packet = receive();
            switch (packet.getOperation()) {
                case Comms.AcceptedConnection:
                    System.out.println("Connected successfully!");
                    break;
                case Comms.DeniedConnection:
                    System.out.println("Connection denied");
                    close();
                    break;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Closes the channel.
     *
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        System.out.println("closing...");
        working = false;
        DataPacket packet = new DataPacket();
        packet.setOperation(Comms.EndConnection);
        send(packet);
        sc.close();
        System.out.println("closed");
    }

    /**
     * Gets working.
     *
     * @return working.
     */
    @Override
    public boolean isWorking() {
        return working;
    }

    /**
     * Sets working
     *
     * @param isWorking
     */
    @Override
    public void setWorking(boolean isWorking) {
        this.working = isWorking;
    }

    /**
     * Sets character.
     *
     * @param character
     */
    @Override
    public void setCharacter(String character) {
        this.character = character;
    }

    /**
     * Sets numPlayer.
     *
     * @param numPlayer
     */
    @Override
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    /**
     * Gets numPlayer.
     *
     * @return numPlayer.
     */
    @Override
    public int getNumPlayer() {
        return numPlayer;
    }

}
