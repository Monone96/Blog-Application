package it.course.myblogc3.service;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.course.myblogc3.entity.PostCost;
import it.course.myblogc3.repository.PostCostRepository;

@Service
public class PostService {
	
	@Autowired
	PostCostRepository postCostRepository;
	
	public boolean isExactMatch(String wordToFind, String source) {
		
		String pattern = "\\b"+wordToFind+"\\b";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(source);

		return m.find();
	}
	
	public int getRealCost(long postId, int initialCost, Date startDate, Date endDate) {
		
		List<PostCost> oldPostCosts = postCostRepository.findActivePostCostByPostIdAndDates(postId, startDate, endDate);
		
		int finalCost = initialCost;
		int shiftCosts = 0;
		
		if(!oldPostCosts.isEmpty())
			shiftCosts = oldPostCosts.stream().mapToInt(p -> p.getShiftCost()).sum();
			finalCost = initialCost + shiftCosts;
		
		return finalCost;
	}
	
	public int getActualRealCost(long postId, int cost) {
		List<PostCost> oldPostCosts = postCostRepository.findActivePostCostByPostId(postId);
		
		int realCost = cost;
		
		if(!oldPostCosts.isEmpty())
			for(PostCost pc : oldPostCosts)
				realCost = realCost + pc.getShiftCost();
		return realCost;
	}
	

}


