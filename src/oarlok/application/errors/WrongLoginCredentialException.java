package oarlok.application.errors;

public class WrongLoginCredentialException extends Exception {
	public WrongLoginCredentialException(String msg){
		super(msg);
	}
}
