package ca.maxx.mountaingen.common;

import java.util.Calendar;
import java.util.Random;

public class NoiseGenerator {
	public  int Seed ;

    public  int Octaves ;

    public  double Amplitude ;

    public  double Persistence ;

    public  double Frequency;

    /**
     * The NoiseGenerator method creates the Perlin Noise for the mountain
     * @param nSeed - The seed for the generator
     * @param nOctives - The number of octives
     * @param dAmplitude - The amplitude 
     * @param dPersistence - The persistence
     * @param dFrequency - The frequency
     */
    public NoiseGenerator(int nSeed, int nOctives, double dAmplitude, double dPersistence, double dFrequency)
    {
    	Calendar cSeed = Calendar.getInstance();
		Random rSeed = new Random();
		
		long lSeed = cSeed.getTimeInMillis() * rSeed.nextLong();
		
		Random r = new Random(lSeed);
        int nOctive = r.nextInt(5);
		int pSeed = r.nextInt(20000) + 30000;
		
        Seed = pSeed;
        Octaves = nOctive;
        Amplitude = 1;
        Frequency = 0.015;
        Persistence = 0.65;
    }
    
    /***
     * This constructor is used to generate the shattered Mountains
     */
    public NoiseGenerator()
    {
    	Calendar cSeed = Calendar.getInstance();
		Random rSeed = new Random();
		
		long lSeed = cSeed.getTimeInMillis() * rSeed.nextLong();
		
		Random r = new Random(lSeed);
		//System.out.println("Perlin Noise seed: " + lSeed);
        int nOctive = r.nextInt(8);
		//System.out.println("Octive: " + nOctive);
		int pSeed = r.nextInt(20000) + 30000;
		//System.out.println("pSeed: " + pSeed);
        Seed = pSeed;
        
        Octaves = 4;
        Amplitude = 1;
        Frequency = 0.015;
        Persistence = 1.65;
    }

    public  double Noise(int x, int y)
    {
        //returns -1 to 1
        double total = 0.0;
        double freq = Frequency, amp = Amplitude;
        for (int i = 0; i < Octaves; ++i)
        {
            total = total + Smooth(x * freq, y * freq) * amp;
            freq *= 2;
            amp *= Persistence;
        }
        if (total < -2.4) total = -2.4;
        else if (total > 2.4) total = 2.4;

        return (total / 2.4);
    }

    public  double NoiseGeneration(int x, int y)
    {
        int n = x + y * 57;
        n = (n << 13) ^ n;

        return (1.0 - ((n * (n * n * 15731 + 789221) + Seed) & 0x7fffffff) / 1073741824.0);
    }

    private  double Interpolate(double x, double y, double a)
    {
        double value = (1 - Math.cos(a * Math.PI)) * 0.5;
        return x * (1 - value) + y * value;
    }

    private  double Smooth(double x, double y)
    {
        double n1 = NoiseGeneration((int)x, (int)y);
        double n2 = NoiseGeneration((int)x + 1, (int)y);
        double n3 = NoiseGeneration((int)x, (int)y + 1);
        double n4 = NoiseGeneration((int)x + 1, (int)y + 1);

        double i1 = Interpolate(n1, n2, x - (int)x);
        double i2 = Interpolate(n3, n4, x - (int)x);

        return Interpolate(i1, i2, y - (int)y);
    }

	public int getSeed() {
		return Seed;
	}

	public void setSeed(int seed) {
		Seed = seed;
	}

	public int getOctaves() {
		return Octaves;
	}

	public void setOctaves(int octaves) {
		Octaves = octaves;
	}

	public double getAmplitude() {
		return Amplitude;
	}

	public void setAmplitude(double amplitude) {
		Amplitude = amplitude;
	}

	public double getPersistence() {
		return Persistence;
	}

	public void setPersistence(double persistence) {
		Persistence = persistence;
	}

	public double getFrequency() {
		return Frequency;
	}

	public void setFrequency(double frequency) {
		Frequency = frequency;
	}
}