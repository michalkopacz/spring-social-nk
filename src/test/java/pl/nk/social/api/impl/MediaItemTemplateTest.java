package pl.nk.social.api.impl;

import static org.junit.Assert.assertEquals;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.social.test.client.RequestMatchers.method;
import static org.springframework.social.test.client.RequestMatchers.requestTo;
import static org.springframework.social.test.client.ResponseCreators.withResponse;

import org.apache.shindig.protocol.RestfulCollection;
import org.apache.shindig.social.opensocial.model.MediaItem;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import pl.nk.opensocial.model.ApplicationMediaItem;
import pl.nk.social.api.MediaItemOperations;

/**
 */
public class MediaItemTemplateTest extends AbstractTemplateTest {

    /**
     * Field oper.
     */
    private MediaItemOperations<MediaItemTemplate> oper;

    /**
     * Method setup.
     */
    @Before
    public void setup() {
        super.setup();
        this.oper = this.nk.mediaItemOperations();
    }

    /**
     * Method getCurrentUserPhotos.
     */
    @Test
    public void getCurrentUserPhotos() {

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        this.mockServer
                .expect(requestTo(oper.getSocialResourceUrl() + "/mediaItems/@me/@self/album.1?startIndex=0&count=20"))
                .andExpect(method(GET)).andRespond(withResponse(jsonResource("mediaitems"), responseHeaders));

        RestfulCollection<ApplicationMediaItem> mediaItems = this.oper.getCurrentUserPhotos("album.1");

        assertEquals(2, mediaItems.getTotalResults());
        ApplicationMediaItem mediaItem = mediaItems.getEntry().get(0);
        assertEquals("mediaitem.82", mediaItem.getId());
        assertEquals("album.1", mediaItem.getAlbumId());
        assertEquals("0", mediaItem.getNumVotes());
        assertEquals("0", mediaItem.getRating());
        assertEquals("2010-03-23T10:41:51.000Z", mediaItem.getCreated());
        assertEquals("http://photos.nasza-klasa.pl/3/3/thumb/8e5e0bfd4a.jpeg", mediaItem.getUrl());
        assertEquals("http://photos.nk-net.pl/22/75/thumb/8cce9c2424.jpeg", mediaItem.getThumbnailUrl());
        assertEquals(MediaItem.Type.IMAGE, mediaItem.getType());
        assertEquals("image/jpeg", mediaItem.getMimeType());
        assertEquals("person.1332123", mediaItem.getAddedBy());
        assertEquals("jhgujk", mediaItem.getDescription());
        assertEquals("0", mediaItem.getNumSuperVotes());
        assertEquals(Long.valueOf(1306926434000L), mediaItem.getNkCreatedTime());
    }
    
    /**
     * Method addCurrentUserPhoto.
     * @throws Exception
     */
    @Test
    public void addCurrentUserPhoto() throws Exception {
        
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_JSON);

        this.mockServer
                .expect(requestTo(oper.getSocialResourceUrl() + "/mediaItems/@me/@self/album.1"))
                .andExpect(method(POST)).andRespond(withResponse(jsonResource("mediaitems"), responseHeaders));
    
        Resource res = new ClassPathResource("/avatar.jpeg");
        this.oper.addCurrentUserPhoto("album.1", res.getInputStream(), "image/jpg", "some description");
        
    }
}
