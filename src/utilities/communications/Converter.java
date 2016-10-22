package utilities.communications;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;

/**
 * Class that defines the conversion methods.
 *
 * @author joan
 */
public class Converter {

    /**
     * Converts objects to byteBuffer.
     *
     * @param obj Object to convert.
     * @return a byteBuffer.
     */
    public ByteBuffer ObtectToByteBuffer(Object obj) {
        ObjectOutputStream ostream;
        ByteArrayOutputStream bstream = new ByteArrayOutputStream();

        try {
            ostream = new ObjectOutputStream(bstream);
            ostream.writeObject(obj);
            ByteBuffer buffer = ByteBuffer.allocate(bstream.size());
            buffer.put(bstream.toByteArray());
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Converts byteBuffers to objects.
     *
     * @param buffer byteBuffer to convert.
     * @return an object.
     */
    public Object ByteBufferToObject(ByteBuffer buffer) {
        byte[] byts = new byte[buffer.limit()];
        buffer.get(byts);
        ObjectInputStream istream = null;
        try {
            istream = new ObjectInputStream(new ByteArrayInputStream(byts));
            Object obj = istream.readObject();
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
