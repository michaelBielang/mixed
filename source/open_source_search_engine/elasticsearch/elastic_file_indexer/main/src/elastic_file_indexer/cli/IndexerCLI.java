package elastic_file_indexer.cli;

import java.util.List;
import java.util.Scanner;

import elastic_file_indexer.document.Document;
import elastic_file_indexer.worker.Indexer;
import elastic_file_indexer.worker.QueryWorker;

public class IndexerCLI {

	public static void main(String[] args) {
		boolean continueFlow = true;
		QueryWorker queryWorker = new QueryWorker();
		Indexer indexer = new Indexer();
		Scanner scanned = new Scanner(System.in);
		System.out.println("Hallo, hier kannst du dein Dateisystem indexieren und sp�ter durchsuchen. "
				+ "Daf�r bitte einen Elasticsearch Server auf dem Standartport 9200 gestartet haben.");
		do {
			System.out.println(
					"\r\nWas m�chtest du machen? i - Index, a - alle indexierten Dateien, q - Query, s - status, c - close");
			String option = scanned.nextLine();

			switch (option) {
			case "i":
				System.out.println("Aus welchem Pfad sollen die Dateien Indexiert werden?");
				String path = scanned.nextLine();
				System.out.println("Indexierung wird gestartet.");
				int fileCount = indexer.indexFilesInPath(path);
				System.out.println(fileCount + " Files wurden indexiert");
				break;
			case "q":
				System.out.println("Mit welcher Query m�chtest du suchen:");
				String query = scanned.nextLine();
				List<Document> hits = queryWorker.querySpecific(query);
				if (hits.size() > 0) {
					System.out.println("Query \"" + query + "\" hat " + hits.size() + " Treffer.");
					hits.forEach(doc -> System.out.println(doc));
				} else
					System.out.println("No Hits.");
				break;
			case "s":
				System.out.println("ClusterStatus: " + queryWorker.clusterInfo());
				break;
			case "a":
				System.out.println("All indexed Files: ");
				queryWorker.queryAllDocuments().forEach(doc -> System.out.println(doc));
				break;
			case "c":
				continueFlow = false;
				break;
			default:
				System.out.println("Nicht valide Eingabe.");
			}
		} while (continueFlow);

		scanned.close();
		System.exit(0);
	}

}
