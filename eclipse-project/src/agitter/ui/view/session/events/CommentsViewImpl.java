package agitter.ui.view.session.events;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

class CommentsViewImpl extends CssLayout implements CommentsView {
	
	private final Label commentLabel = new Label("Comentários:"); 
	private final TextArea comment = new TextArea();
	private final NativeButton commentButton = AgitterVaadinUtils.createDefaultAddButton();
	private final Label commentList = new Label();
	
	private Boss boss;

	
	{
		addComponent(commentLabel);
		
		comment.setNullRepresentation("");
		comment.setInputPrompt("O que Achou?");
		comment.setSizeUndefined();
		
		addComponent(comment);
		comment.addStyleName("a-new-comment");
		
		commentButton.addStyleName("a-comment-post-ignored");
		addComponent(commentButton);
		
		addComponent(commentList);
	}
	
	
	@Override
	public void startReportingTo(Boss b) {
		if (this.boss != null) throw new IllegalStateException();
		this.boss = b;
		commentButton.addListener(new ClickListener() { @Override public void buttonClick(ClickEvent event) {
			commentPosted();
		}});
	}

	
	@Override public void hide() { setVisibility(false); }
	@Override public void show() { if ( COMMENTS_ENABLED ) setVisibility(true); }


	private void setVisibility(boolean v) {
		commentLabel.setVisible(v); 
		comment.setVisible(v);
		commentButton.setVisible(v);
		commentList.setVisible(v);
	}
	
	private void commentPosted() {
		boss.onCommentPosted((String)comment.getValue());
	}
	
	
	@Override
	public void addComment(String user, String date, String text) {
		commentList.setValue(commentList.getValue() + "--------- Usuário: " + user + " Data: " + date + " Texto: " + text);
	}
	
	@Override
	public void clearCommentBox() {
		comment.setValue("");
	}


	@Override
	public void clearCommentList() {
		commentList.setValue("");
	}

	
	private final CssLayout popup = new CssLayout();
	private final TextField nameTf = new TextField( "Nome" );
	private final NativeButton okButton = AgitterVaadinUtils.createDefaultNativeButton("OK");
	private final NativeButton cancelButton = AgitterVaadinUtils.createDefaultNativeButton("Cancelar");
	
	{
		popup.addStyleName( "a-comment-ask-name-view" ); //MMM esta no css errado
		popup.setVisible(false);
		addComponent( popup );

		okButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				onNameGiven();
			}
		});
		cancelButton.addListener(new ClickListener() {
			@Override
			public void buttonClick(ClickEvent event) {
				disposePopup();
			}
		});
	}
	
	@Override
	public void askForName() {
		setModalEnablement(false);

		nameTf.setInputPrompt("Por favor informe seu nome");
		nameTf.setValue( "" );
		popup.addComponent(nameTf); ////MMM criar um CSS

		okButton.addStyleName("a-login-button"); //MMM arrumar CSS
		popup.addComponent(okButton);

		cancelButton.addStyleName("a-login-cancel-button"); //MMM arrumar CSS
		popup.addComponent(cancelButton);

		popup.setVisible(true);
		
		WidgetUtils.focusOrder(nameTf, okButton, cancelButton);
		nameTf.focus();
	}


	private void setModalEnablement(boolean b) {
		comment.setEnabled(b);
		commentButton.setEnabled(b);
	}

	private void onNameGiven() {
		if(boss.onNameGiven((String)nameTf.getValue())) {
			commentPosted();
			setModalEnablement(true);
			disposePopup();
		}
	}
	
	private void disposePopup() {
		setModalEnablement(true);
		popup.removeAllComponents();
		popup.setVisible(false);		
	}

}
