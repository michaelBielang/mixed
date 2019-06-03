package elastic_file_indexer.worker;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.elasticsearch.action.main.MainResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import elastic_file_indexer.document.Document;

public class QueryWorker extends AbstractWorker {

	public QueryWorker() {
		super();
	}

	public MainResponse clusterInfo() {
		try {
			return client.info(RequestOptions.DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public SearchHits queryAllDocuments() {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.matchAllQuery());
		SearchRequest searchRequest = new SearchRequest();
		return this.getAllAvailable(searchSourceBuilder, searchRequest);
	}

	public List<Document> querySpecific(String query) {
		SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
		searchSourceBuilder.query(QueryBuilders.queryStringQuery(query));
		SearchRequest searchRequest = new SearchRequest();
		searchRequest.indices("*");

		List<Document> docs = new LinkedList<Document>();
		this.getAllAvailable(searchSourceBuilder, searchRequest).forEach(doc -> {
			Map<String, Object> fields = doc.getSourceAsMap();
			docs.add(new Document((String) fields.get("path"),
					LocalDateTime.ofInstant(Instant.parse((String) fields.get("creationTime")),
							ZoneOffset.systemDefault()),
					LocalDateTime.ofInstant(Instant.parse((String) fields.get("lastAccessTime")),
							ZoneOffset.systemDefault()),
					LocalDateTime.ofInstant(Instant.parse((String) fields.get("lastModifiedTime")),
							ZoneOffset.systemDefault()),
					Long.valueOf((int) fields.get("size"))));
		});
		return docs;

	}

	private SearchHits getAllAvailable(SearchSourceBuilder searchSourceBuilder, SearchRequest searchRequest) {
		searchSourceBuilder.size(0);
		searchRequest.source(searchSourceBuilder);
		try {
			SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			searchSourceBuilder.size((int) searchResponse.getHits().getTotalHits().value);
			searchRequest.source(searchSourceBuilder);
			searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
			return searchResponse.getHits();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

}
