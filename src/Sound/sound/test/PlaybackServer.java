package Sound.sound.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

class PlaybackServer {

	private class Worker implements Runnable {
		@Override
		public void run() {
			int bufferSize = Util.getBufferSize(format, Util.BUFFER_TIME);
			DataLine.Info line = PlaybackServer.this.line;
			if (line == null)
				line = new DataLine.Info(SourceDataLine.class, format, bufferSize);

			SourceDataLine soundLine = null;

			try {
				soundLine = (SourceDataLine) AudioSystem.getLine(line);
				soundLine.open(format, bufferSize);
			} catch (LineUnavailableException e) {
				e.printStackTrace();
				return;
			}

			soundLine.start();

			byte[] buffer = new byte[64 * 1024];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

			try {
				while (true) {
					server.receive(packet);
					soundLine.write(buffer, 0, packet.getLength());
				}
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}
		}
	}

	private final AudioFormat format;
	private final DataLine.Info line;
	private final DatagramSocket server;
	private final Thread thread;

	public PlaybackServer(int port, DataLine.Info line, AudioFormat format) throws IOException {
		this.format = format;
		this.line = line;
		this.server = new DatagramSocket(port);

		this.thread = new Thread(new Worker());
		this.thread.start();
	}

	public static void main(String[] args) throws Throwable {
		int port = Util.PORT;
		if (args.length > 0)
			port = Integer.parseInt(args[0]);
		new PlaybackServer(port, null, Util.FORMAT);
	}

}
