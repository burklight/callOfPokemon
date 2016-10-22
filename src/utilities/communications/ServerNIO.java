package utilities.communications;

import server.database.DataBase;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import server.controllers.DataBaseController;
import server.models.GameModel;

/**
 * This class controls the NIO connection of the server.
 */
public class ServerNIO {

    protected ServerSocketChannel ssc;
    protected Selector selector;
    protected ArrayList<SocketChannel> clients;
    protected Converter converter;
    protected GameModel game;
    protected DataBase db;
    protected DataBaseController dbc;

    public ServerNIO() throws Exception {
        converter = new Converter();
        clients = new ArrayList<>();
        db = new DataBase();
        db.getConnection();
        dbc = new DataBaseController(db);
        selector = Selector.open();
        System.out.println("Selector open: " + selector.isOpen());
        ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(Comms.serverPort));
        ssc.configureBlocking(false);
        ssc.register(selector, ssc.validOps());

    }

    /**
     * Converts the given object to byteBuffer then sends it through the given
     * SocketChannel.
     *
     * @param o
     * @param sc
     * @throws IOException
     */
    public void writeObject(Object o, SocketChannel sc) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(Comms.SIZE_BUFFER);
        ByteBuffer b = converter.ObtectToByteBuffer(o);
        bb.put(b.array());
        bb.flip();
        sc.write(bb);
        bb.clear();
    }

    /**
     * Reads a byteBuffer from the given SocketChannel then converts it to
     * object.
     *
     * @param sc
     * @return object.
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object readByteBuffer(SocketChannel sc) throws IOException, ClassNotFoundException {
        ByteBuffer bb = ByteBuffer.allocate(Comms.SIZE_BUFFER);
        sc.read(bb);
        bb.flip();
        Object obj = converter.ByteBufferToObject(bb);
        return obj;
    }

    /**
     * Converts the given DataPacket to byteBuffer and sends it to all the
     * clients.
     *
     * @param packet
     * @throws IOException
     */
    public void broadcast(DataPacket packet) throws IOException {
        ByteBuffer bb = ByteBuffer.allocate(Comms.SIZE_BUFFER);
        bb = converter.ObtectToByteBuffer(packet);
        bb.flip();
        for (SocketChannel sc : clients) {
            sc.write(bb);
            bb.flip();
        }
    }

    /**
     * Gets selector.
     *
     * @return selector.
     */
    public Selector getSelector() {
        return selector;
    }

    /**
     * Adds a socketChannel to the list of clients.
     *
     * @param sc
     */
    public void addClient(SocketChannel sc) {
        clients.add(sc);
    }

    /**
     * Removes the given socketChannel from the list of clients.
     *
     * @param sc
     */
    public void removeClient(SocketChannel sc) {
        clients.remove(sc);
    }

    /**
     * Removes all the socketChannels from the list of clients.
     */
    public void removeAllClients() {
        for (SocketChannel sc : clients) {
            clients.remove(sc);
        }
    }

    /**
     * Gets clients.
     *
     * @return clients.
     */
    public ArrayList<SocketChannel> getClients() {
        return clients;
    }

    /**
     * Gets dbc.
     *
     * @return dbc.
     */
    public DataBaseController getDbc() {
        return dbc;
    }

}
