import java.util.Arrays;
import org.concentus.OpusEncoder;
import org.concentus.OpusApplication;
import org.concentus.OpusDecoder;
import org.concentus.OpusException;

public class Opus {

    private final int SAMPLE_RATE = 48000;
    private final int CHANNELS = 1;
    private final int FRAME_SIZE = 960;

    private OpusEncoder encoder;
    private OpusDecoder decoder;

    Opus() {
        try {
            encoder = new OpusEncoder(SAMPLE_RATE, CHANNELS, OpusApplication.OPUS_APPLICATION_AUDIO);
            decoder = new OpusDecoder(SAMPLE_RATE, CHANNELS);
        } catch (OpusException e) {
            throw new RuntimeException(e);
        }
    }

    public byte[] encode(byte[] pcmBytes) throws Exception {
        short[] pcmShorts = bytesToShorts(pcmBytes);
        byte[] encoded = new byte[4000];

        int encodedBytes = encoder.encode(pcmShorts, 0, FRAME_SIZE, encoded, 0, encoded.length);
        return Arrays.copyOf(encoded, encodedBytes);
    }


    public byte[] decode(byte[] encodedBytes) throws Exception {
        short[] decodedShorts = new short[FRAME_SIZE * CHANNELS];
        int decodedSamples = decoder.decode(encodedBytes, 0, encodedBytes.length, decodedShorts, 0, FRAME_SIZE, false);

        return shortsToBytes(Arrays.copyOf(decodedShorts, decodedSamples));
    }

    public static short[] bytesToShorts(byte[] bytes) {
        int len = bytes.length / 2;
        short[] shorts = new short[len];
        for (int i = 0; i < len; i++) {
            shorts[i] = (short) ((bytes[i * 2] << 8) | (bytes[i * 2 + 1] & 0xFF));
        }
        return shorts;
    }

    private static byte[] shortsToBytes(short[] shorts) {
        byte[] bytes = new byte[shorts.length * 2];
        for (int i = 0; i < shorts.length; i++) {
            bytes[i * 2] = (byte) (shorts[i] & 0xFF);
            bytes[i * 2 + 1] = (byte) ((shorts[i] >> 8) & 0xFF);
        }
        return bytes;
    }
}
