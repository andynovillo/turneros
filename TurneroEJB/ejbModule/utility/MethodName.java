package utility;

public class MethodName {

	public static final int CLIENT_CODE_STACK_INDEX;

	static {

		int index = 0;

		for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
			index++;
			if (stackTraceElement.getClassName().equals(MethodName.class.getName())) {
				break;
			}
		}

		CLIENT_CODE_STACK_INDEX = index;

	}

	public static String methodName() {
		return Thread.currentThread().getStackTrace()[CLIENT_CODE_STACK_INDEX].getMethodName();
	}

}
