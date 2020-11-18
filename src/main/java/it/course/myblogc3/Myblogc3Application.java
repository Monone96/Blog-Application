package it.course.myblogc3;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;

import it.course.myblogc3.entity.Authority;
import it.course.myblogc3.entity.AuthorityName;
import it.course.myblogc3.entity.Language;
import it.course.myblogc3.entity.Voting;
import it.course.myblogc3.entity.VotingId;
import it.course.myblogc3.entity.Post;
import it.course.myblogc3.entity.User;
import it.course.myblogc3.entity.Comment;
import it.course.myblogc3.entity.Tag;
import it.course.myblogc3.repository.LanguageRepository;
import it.course.myblogc3.repository.PostRepository;
import it.course.myblogc3.repository.TagRepository;
import it.course.myblogc3.repository.TokenRepository;
import it.course.myblogc3.repository.UserRepository;
import it.course.myblogc3.repository.VotingRepository;

@EnableScheduling
@SpringBootApplication
public class Myblogc3Application {
	
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private LanguageRepository languageRepository;
	@Autowired
	private TagRepository tagRepository;
	@Autowired
	VotingRepository votingRepository;
	@Autowired
	TokenRepository tokenRepository;
	
	@Scheduled(cron = "0 0 10 * * *")
	@Transactional
	public void deleteOldTokens() {
		tokenRepository.deleteAllByExpiryDateLessThan(new Date());
	}

	public static void main(String[] args) {
		SpringApplication.run(Myblogc3Application.class, args);
	}
	
	@Bean
	@Profile("dev")
	@Transactional
	InitializingBean sendDatabase() {
	    return () -> {
	    	
	    	String title = "Title_";
	    	if(!postRepository.existsByTitleStartsWith(title)) {
	    	
		    	Random rand = new Random();
		    		    		    	
		    	Set<Authority> setEditor = new HashSet<>();
		    	setEditor.add(new Authority(2L, AuthorityName.ROLE_EDITOR));
		    	
		    	Set<Authority> setReader = new HashSet<>();
		    	setReader.add(new Authority(3L, AuthorityName.ROLE_READER));
		    	
		    	List<User> listEditor = userRepository.findAllByAuthoritiesIn(setEditor);
		    	List<User> listReader = userRepository.findAllByAuthoritiesIn(setReader);
		    	List<Tag> tags = tagRepository.findAllByVisibleTrue();
		    	
		    	List<Language> ll = languageRepository.findAllByVisibleTrue();
		    	
		    	String content = "Lorem ipsum dolor sit amet, consectetur adipisci elit, sed do eiusmod tempor incidunt ut labore et dolore magna aliqua."
		    			+ " Ut enim ad minim veniam, quis nostrum exercitationem ullamco laboriosam, nisi ut aliquid ex ea commodi consequatur. "
		    			+ "Duis aute irure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. "
		    			+ "Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum";
		    	String comment = "Comment_";
		    	
		    	List<Post> ps =  new ArrayList<Post>();
		    	List<Voting> vs = new ArrayList<Voting>();
		    	
		    	for(int n=0; n<20; n++) {
		    		Post p = new Post(
	    				title + n,
	    				content,
	    				listEditor.get(rand.nextInt(listEditor.size())),
	    				ll.get(rand.nextInt(ll.size())),
	    				rand.nextBoolean()	    				
	    				);
		    		
		    		if(p.getVisible()) {
		    			//comments
		    			List<Comment> lc =  new ArrayList<Comment>();
		    			for(int j=0; j<10; j++) {
			    			Comment c = new Comment(
		    					comment+j,
		    					p,
		    					listReader.get(rand.nextInt(listReader.size())),
		    					rand.nextBoolean()
			    				);
			    			lc.add(c);
			    		}
		    			//tags
		    			Set<Tag> tg = new HashSet<>();
		    			int[] array = {1,2,3};
		    			int limit = array[rand.nextInt(array.length)];
		    			for(int j=0; j<limit; j++) {
		    				tg.add(tags.get(rand.nextInt(tags.size())));		    				
		    			}
		    			//voting
		    			for(int k=0; k<listReader.size(); k++) {
		    				vs.add(new Voting
		    						(new VotingId(p, listReader.get(k)),
		    						(int)(Math.random() * 5) + 1) 
		    						);
		    			}
		    			p.setTags(tg);
		    			p.setComments(lc);
		    		}
		    		ps.add(p);
		    	}
		    	postRepository.saveAll(ps);
		    	votingRepository.saveAll(vs);
	    	}
	    };
	}
	
}
	    				
