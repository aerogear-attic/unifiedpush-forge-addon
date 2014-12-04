package ${package}.push;

import org.jboss.aerogear.unifiedpush.message.UnifiedMessage;

public interface SenderService {

	void send(UnifiedMessage message); 
}
