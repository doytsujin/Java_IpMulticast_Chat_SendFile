package Sound.sound.test;

import java.net.InetSocketAddress;

import javax.sound.sampled.AudioFormat;

public class Util {

	public static final String HOST = "localhost";
	public static final int PORT = 12345;
	public static final int TIMEOUT = 1000;

	public static final AudioFormat FORMAT = new AudioFormat(44100, 8, 1, true, true);
	public static final double BUFFER_TIME = 0.2;

	public static int getBufferSize(AudioFormat format, double bufferTime) {
		return (int) Math.ceil(bufferTime * format.getSampleRate() * format.getSampleSizeInBits() / 8);
	}

	public static InetSocketAddress parse(String s) {
		String host = HOST;
		int port = PORT;

		if (!s.isEmpty()) {
			String[] tmp = s.split(":");
			host = tmp[0];
			if (tmp.length > 1)
				port = Integer.parseInt(tmp[1]);
		}

		return new InetSocketAddress(host, port);
	}

	private Util() {
	}

}
