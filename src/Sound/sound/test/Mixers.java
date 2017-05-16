package Sound.sound.test;

import java.util.Arrays;
import java.util.stream.Stream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;

public class Mixers {

	public static void main(String[] args) {
		Mixer.Info[] mixers = AudioSystem.getMixerInfo();
		for (Mixer.Info mixerInfo : mixers) {
			System.out.println("Found Mixer: " + mixerInfo);

			Mixer m = AudioSystem.getMixer(mixerInfo);

			Line.Info[] sl = m.getSourceLineInfo();
			Line.Info[] tl = m.getTargetLineInfo();

			Stream.concat(Arrays.stream(sl), Arrays.stream(tl)).forEach(li -> {
				System.out.println("Found target line: " + li);
				try {
					m.open();
					m.close();
				} catch (LineUnavailableException e) {
					System.out.println("Line unavailable.");
				}

				if (li instanceof DataLine.Info) {
					DataLine.Info dli = (DataLine.Info) li;
					AudioFormat[] formats = dli.getFormats();

					for (AudioFormat f : formats) {
						System.out.println(f);
					}
				}
			});
		}

	}

}
