package org.schabi.newpipe.extractor.feed;

import org.schabi.newpipe.extractor.ListExtractor.InfoItemsPage;
import org.schabi.newpipe.extractor.ListInfo;
import org.schabi.newpipe.extractor.NewPipe;
import org.schabi.newpipe.extractor.StreamingService;
import org.schabi.newpipe.extractor.exceptions.ExtractionException;
import org.schabi.newpipe.extractor.stream.StreamInfoItem;
import org.schabi.newpipe.extractor.utils.ExtractorHelper;

import java.io.IOException;
import java.util.List;

public class FeedInfo extends ListInfo<StreamInfoItem> {

    public FeedInfo(int serviceId, String id, String url, String originalUrl, String name, List<String> contentFilter, String sortFilter) {
        super(serviceId, id, url, originalUrl, name, contentFilter, sortFilter);
    }

    public static FeedInfo getInfo(String url) throws IOException, ExtractionException {
        return getInfo(NewPipe.getServiceByUrl(url), url);
    }

    public static FeedInfo getInfo(StreamingService service, String url) throws IOException, ExtractionException {
        final FeedExtractor extractor = service.getFeedExtractor(url);

        if (extractor == null) {
            throw new IllegalArgumentException("Service \"" + service.getServiceInfo().getName() + "\" doesn't support FeedExtractor.");
        }

        extractor.fetchPage();
        return getInfo(extractor);
    }

    public static FeedInfo getInfo(FeedExtractor extractor) throws IOException, ExtractionException {
        extractor.fetchPage();

        final int serviceId = extractor.getServiceId();
        final String id = extractor.getId();
        final String url = extractor.getUrl();
        final String originalUrl = extractor.getOriginalUrl();
        final String name = extractor.getName();

        final FeedInfo info = new FeedInfo(serviceId, id, url, originalUrl, name, null, null);

        final InfoItemsPage<StreamInfoItem> itemsPage = ExtractorHelper.getItemsPageOrLogError(info, extractor);
        info.setRelatedItems(itemsPage.getItems());
        info.setNextPageUrl(itemsPage.getNextPageUrl());

        return info;
    }
}
