package ar.edu.itba.paw.services.simple;

import ar.edu.itba.paw.interfaces.dao.JobPostImageDao;
import ar.edu.itba.paw.interfaces.services.ImageService;
import ar.edu.itba.paw.interfaces.services.JobPostImageService;
import ar.edu.itba.paw.models.ByteImage;
import ar.edu.itba.paw.models.JobPostImage;
import exceptions.JobPostImageNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class SimpleJobPostImageService implements JobPostImageService {

	private final int MAX_IMAGES_NUMBER = 5;

	@Autowired
	private JobPostImageDao jobPostImageDao;

	@Autowired
	private ImageService imageService;

	@Override
	public JobPostImage addImage(long postId, ByteImage image) {

		if(!imageService.isValidImage(image))
			throw new IllegalArgumentException("Invalid byte image");
		if(maxImagesUploaded(postId))
			return null;

		return jobPostImageDao.addImage(postId, image);
	}

	@Override
	public List<JobPostImage> addImages(long postId, List<ByteImage> byteImages) {
		List<JobPostImage> images = new ArrayList<>();
		for(ByteImage image : byteImages){
			if(!imageService.isValidImage(image))
				throw new IllegalArgumentException("Invalid byte image");
			if(maxImagesUploaded(postId))
				break;
			images.add(jobPostImageDao.addImage(postId, image));
		}
		return images;
	}

	@Override
	public boolean maxImagesUploaded(long postId) {
		return jobPostImageDao.getImageCount(postId) >= MAX_IMAGES_NUMBER;
	}

	@Override
	public JobPostImage findById(long imageId) {
		return jobPostImageDao.findById(imageId).orElseThrow(JobPostImageNotFoundException::new);
	}

	@Override
	public List<Long> getImagesIdsByPostId(long postId) {
		return jobPostImageDao.getImagesIdsByPostId(postId);
	}
}
