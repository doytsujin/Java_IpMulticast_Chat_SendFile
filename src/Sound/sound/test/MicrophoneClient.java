package Sound.sound.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.TargetDataLine;

public class MicrophoneClient {

	private class Worker implements Runnable {
		@Override
		public void run() {
			byte[] buffer = new byte[bufferSize];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress);

			try {
				while (true) {
					int read = soundLine.read(buffer, 0, buffer.length);
					packet.setLength(read);
					socket.send(packet);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private final SocketAddress socketAddress;
	private final DatagramSocket socket;
	private final int bufferSize;
	private final TargetDataLine soundLine;
	private final Thread thread;

	public MicrophoneClient(SocketAddress socketAddress, DataLine.Info line, AudioFormat format, double bufferTime)
			throws SocketException, LineUnavailableException {
		this.socketAddress = socketAddress;
		this.socket = new DatagramSocket();

		this.bufferSize = Util.getBufferSize(format, bufferTime);
		if (line == null)
			line = new DataLine.Info(TargetDataLine.class, format, bufferSize);

		soundLine = (TargetDataLine) AudioSystem.getLine(line);
		soundLine.open(format, bufferSize);
		soundLine.start();

		this.thread = new Thread(new Worker());
		this.thread.start();
	}

	public static void main(String[] args) throws Throwable {
		SocketAddress socketAddress = new InetSocketAddress("localhost", Util.PORT);
		if (args.length > 0)
			socketAddress = Util.parse(args[0]);
		new MicrophoneClient(socketAddress, null, Util.FORMAT, Util.BUFFER_TIME);
	}

}
