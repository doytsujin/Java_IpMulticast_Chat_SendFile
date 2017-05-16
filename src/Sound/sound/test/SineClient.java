package Sound.sound.test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;

import javax.sound.sampled.AudioFormat;

public class SineClient {

	private class Worker implements Runnable {
		@Override
		public void run() {
			double sampleTime = 1 / format.getSampleRate();
			int samples = packetSize / (format.getSampleSizeInBits() / 8);
			long wait = (long) (1000 * samples * sampleTime);
			byte[] buffer = new byte[samples];
			DatagramPacket packet = new DatagramPacket(buffer, buffer.length, socketAddress);

			double time = 0;

			try {
				while (true) {
					for (int i = 0; i < samples; i++) {
						buffer[i] = (byte) (127 * Math.sin(2 * Math.PI * frequency * time));
						time += sampleTime;
					}

					socket.send(packet);
					Thread.sleep(wait);
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	};

	private final AudioFormat format;
	private final double frequency;
	private final int packetSize;

	private final SocketAddress socketAddress;
	private final DatagramSocket socket;

	private final Thread thread;

	public SineClient(SocketAddress socketAddress, AudioFormat format, double frequency, int packetSize)
			throws SocketException {
		this.format = format;
		this.frequency = frequency;
		this.packetSize = packetSize;

		this.socketAddress = socketAddress;
		this.socket = new DatagramSocket();

		this.thread = new Thread(new Worker());
		this.thread.start();
	}

	public static void main(String[] args) throws SocketException {
		SocketAddress socketAddress = new InetSocketAddress("localhost", Util.PORT);
		if (args.length > 0)
			socketAddress = Util.parse(args[0]);
		new SineClient(socketAddress, Util.FORMAT, 440, 1024);
	}

}
