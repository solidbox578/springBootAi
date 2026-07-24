package com.vibhu.openai.rag;

import jakarta.annotation.PostConstruct;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HRPolicyLoader {

    private final VectorStore vectorStore;

    @Value("classpath:Eazybytes_HR_Policies.pdf")
    Resource policyFile;

    public HRPolicyLoader(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadPolicies() {

        SearchRequest request = SearchRequest.builder()
                .query("Leave Policy")
                .topK(1)
                .build();

        if (!vectorStore.similaritySearch(request).isEmpty()) {
            return;
        }


        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
        List<Document> documents = tikaDocumentReader.get();
        TokenTextSplitter tokenTextSplitter = TokenTextSplitter.builder().withChunkSize(100).withMaxNumChunks(400).build();
        documents = tokenTextSplitter.split(documents);
        vectorStore.add(documents);
    }

}
