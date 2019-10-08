package utility;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;

public class GenericPrinterSocket {

	private static final Integer port = 59999;
	private Socket clientSocket = null;
	private ObjectOutputStream objectOutputStream = null;
	private ObjectInputStream objectInputStream = null;
	private Boolean printed;

	private static final Integer connectionTimeOut = 12000;
	private static final Integer operationTimeOut = 12000;

	public GenericPrinterSocket() {

		printed = false;

	}

	@SuppressWarnings("unchecked")
	public void sendTicket(HashMap<String, Object> data) {

		printed = false;

		try {

			Boolean logStatus = data.get("logStatus") != null ? (Boolean) data.get("logStatus") : false;

			clientSocket = new Socket();

			clientSocket.connect(new InetSocketAddress(data.get("ip").toString(), port), connectionTimeOut);
			clientSocket.setSoTimeout(operationTimeOut);

			objectOutputStream = new ObjectOutputStream(clientSocket.getOutputStream());
			objectOutputStream.writeObject(data);

			objectInputStream = new ObjectInputStream(clientSocket.getInputStream());
			HashMap<String, Object> result = (HashMap<String, Object>) objectInputStream.readObject();

			if (logStatus)
				System.out.println(result.get("text"));
			printed = (Boolean) result.get("printed");

			if (objectInputStream != null) {
				objectInputStream.close();
			}
			if (objectOutputStream != null) {
				objectOutputStream.close();
			}

		} catch (IOException | ClassNotFoundException exception) {
			System.out.println("Can't send data to print from " + data.get("ip").toString());
			System.out.println("Exception: " + exception.getMessage());
		} finally {
			try {
				if (clientSocket != null) {
					clientSocket.close();
				}
			} catch (IOException exception) {
				System.out.println("Can't close connection from " + data.get("ip").toString());
				System.out.println("Exception: " + exception.getMessage());
			}
		}

	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}

	public ObjectOutputStream getObjectOutputStream() {
		return objectOutputStream;
	}

	public void setObjectOutputStream(ObjectOutputStream objectOutputStream) {
		this.objectOutputStream = objectOutputStream;
	}

	public ObjectInputStream getObjectInputStream() {
		return objectInputStream;
	}

	public void setObjectInputStream(ObjectInputStream objectInputStream) {
		this.objectInputStream = objectInputStream;
	}

	public Boolean getPrinted() {
		return printed;
	}

	public void setPrinted(Boolean printed) {
		this.printed = printed;
	}

}
