package test12;
import com.embeddedunveiled.serial.SerialComManager;
import com.embeddedunveiled.serial.SerialComManager.BAUDRATE;
import com.embeddedunveiled.serial.SerialComManager.DATABITS;
import com.embeddedunveiled.serial.SerialComManager.ENDIAN;
import com.embeddedunveiled.serial.SerialComManager.FLOWCONTROL;
import com.embeddedunveiled.serial.SerialComManager.NUMOFBYTES;
import com.embeddedunveiled.serial.SerialComManager.PARITY;
import com.embeddedunveiled.serial.SerialComManager.STOPBITS;

public class Test12 {
	public static void main(String[] args) {
		
		SerialComManager scm = new SerialComManager();
		
		try {
			// open and configure port that will listen data
			long handle = scm.openComPort("/dev/ttyUSB1", true, true, false);
			scm.configureComPortData(handle, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			// open and configure port which will send data
			long handle1 = scm.openComPort("/dev/ttyUSB0", true, true, false);
			scm.configureComPortData(handle1, DATABITS.DB_8, STOPBITS.SB_1, PARITY.P_NONE, BAUDRATE.B115200, 0);
			scm.configureComPortControl(handle1, FLOWCONTROL.NONE, 'x', 'x', false, false);
			
			// 350 = 00000001 01011110, 2 bytes required, big endian format
			scm.writeSingleInt(handle, 350, 0, ENDIAN.E_BIG, NUMOFBYTES.NUM_2);
			Thread.sleep(100);
			byte[] arr = new byte[2];
			arr = scm.readBytes(handle1, 2);
			System.out.println("dataa: " + arr[0]); // prints 1  which is 00000001
			System.out.println("datab: " + arr[1]); // prints 94 which is 01011110
			
			// 350 = 00000001 01011110, 2 bytes required, little endian format
			scm.writeSingleInt(handle, 350, 0, ENDIAN.E_LITTLE, NUMOFBYTES.NUM_2);
			Thread.sleep(100);
			byte[] arr1 = new byte[2];
			arr1 = scm.readBytes(handle1, 2);
			System.out.println("dataa: " + arr1[0]); // prints 94 which is 01011110
			System.out.println("datab: " + arr1[1]); // prints 1  which is 00000001
			
			// 7050354 = 00000000 01101011 10010100 01110010, 4 bytes required, big endian format
			scm.writeSingleInt(handle, 7050354, 0, ENDIAN.E_BIG, NUMOFBYTES.NUM_4);
			Thread.sleep(100);
			byte[] arr2 = new byte[4];
			arr2 = scm.readBytes(handle1, 4);
			System.out.println("dataa: " + arr2[0]); // prints 0    which is 00000000
			System.out.println("datab: " + arr2[1]); // prints 107  which is 01101011
			System.out.println("datac: " + arr2[2]); // prints -108 which is 10010100
			System.out.println("datad: " + arr2[3]); // prints 114  which is 01110010
			
			// 7050354 = 00000000 01101011 10010100 01110010, 4 bytes required, little endian format
			scm.writeSingleInt(handle, 7050354, 0, ENDIAN.E_LITTLE, NUMOFBYTES.NUM_4);
			Thread.sleep(100);
			byte[] arr3 = new byte[4];
			arr3 = scm.readBytes(handle1, 4);
			System.out.println("dataa: " + arr3[0]); // prints 114  which is 01110010
			System.out.println("datab: " + arr3[1]); // prints -108 which is 10010100
			System.out.println("datac: " + arr3[2]); // prints 107  which is 01101011
			System.out.println("datad: " + arr3[3]); // prints 0    which is 00000000
			
			scm.closeComPort(handle);
			scm.closeComPort(handle1);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}