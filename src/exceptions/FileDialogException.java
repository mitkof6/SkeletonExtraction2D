package exceptions;

/**
 * This class is responsible for the open/save file dialog exception.
 * 
 * @author Jim Stanev
 */
public class FileDialogException extends CustomException{

	private static final long serialVersionUID = 1L;

	/**
	 * Constructor.
	 * 
	 * @param message the message of the exception
	 */
	public FileDialogException(String message) {
		super(message);
	}

	
}
