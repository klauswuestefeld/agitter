package agitter.ui.view.session.events;

import vaadinutils.WidgetUtils;
import agitter.ui.view.AgitterVaadinUtils;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.BlurEvent;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;

class CommentsViewImpl extends CssLayout implements CommentsView {
	
	private final CssLayout popup = new CssLayout();
	private final TextField nameTf = new TextField( "Você deve ter um nome cadastrado:" );
	private final NativeButton okButton = AgitterVaadinUtils.createDefaultNativeButton("OK");
	private final NativeButton cancelButton = AgitterVaadinUtils.createDefaultNativeButton("Cancelar");
	
	{
		popup.addStyleName( "a-comment-ask-name-view" ); //MMM esta no css errado
		popup.setVisible(false);

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

	private final Label commentLabel = new Label("Comentários:"); 
	private final TextArea comment = new TextArea();
	private final NativeButton commentButton = AgitterVaadinUtils.createDefaultAddButton();
	private final CssLayout commentList = new CssLayout();
	
	private Boss boss;

	{
		commentLabel.addStyleName("a-comments-view-label");
		addComponent(commentLabel);
		
		comment.setNullRepresentation("");
		comment.setInputPrompt("Escreva seu comentário...");
		comment.setSizeUndefined();
		comment.setMaxLength(500);
		comment.addStyleName("a-comments-view-text");
		addComponent(comment);
		
		commentButton.addStyleName("a-comments-view-post");
		addComponent(commentButton);

		addComponent(popup);
		
		commentList.addStyleName("a-comments-view-list");
		addComponent(commentList);
		
		addStyleName("a-comments-view");
		
		final String withFocusStyleName = "a-comments-view-text-withfocus"; 
		comment.addListener(new FieldEvents.FocusListener() {
			@Override
			public void focus(FocusEvent event) {
				comment.addStyleName(withFocusStyleName);
			}
		});
		comment.addListener(new FieldEvents.BlurListener() {
			@Override
			public void blur(BlurEvent event) {
				comment.removeStyleName(withFocusStyleName);
			}
		});
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
		String text = (String)comment.getValue();
		if(text!=null && !"".equals(text)) {
			boss.onCommentPosted(text);
		}
	}
	
	
	@Override
	public void addComment(String user, String date, String text) {
		commentList.addComponent(new CommentComponent(user, text, date));
	}
	
	@Override
	public void clearCommentBox() {
		comment.setValue("");
	}


	@Override
	public void clearCommentList() {
		commentList.removeAllComponents();
	}

	
	@Override
	public void askForName() {
		setModalEnablement(false);

		nameTf.setInputPrompt("Por favor informe seu nome");
		nameTf.setValue("");
		nameTf.setColumns(15);
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
	
	
	private class CommentComponent extends CssLayout {
		
		public CommentComponent(String user, String text, String date) {
			this.addStyleName( "a-comment-view" );

			Label userLabel = new Label(user);
			userLabel.addStyleName( "a-comment-view-user" );
			this.addComponent(userLabel);

			Label textLabel = new Label(text);
			textLabel.setContentMode(Label.CONTENT_XHTML);
			textLabel.addStyleName( "a-comment-view-text" );
			this.addComponent(textLabel);
			
			Label dateLabel = new Label(date);
			dateLabel.addStyleName( "a-comment-view-date" );
			this.addComponent(dateLabel);
		}
		
	}
	
}
