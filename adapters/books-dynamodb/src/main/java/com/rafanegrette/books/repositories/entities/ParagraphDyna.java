package com.rafanegrette.books.repositories.entities;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
@AllArgsConstructor
@NoArgsConstructor
public class ParagraphDyna implements Serializable {

	private static final long serialVersionUID = -2011581583666716620L;
	private Integer id;
    private List<SentenceDyna> sentences;
	
    public enum ParagraphSeparator {
        ONE_JUMP_WITH_SPACE(". \n"),
        TWO_JUMP("\n\n"),
        ONE_JUMP("[.|”]\n");
        private String separator;
        private ParagraphSeparator(String separator) {
            this.separator = separator;
        }
        
        public String getSeparator() {
            return separator;
        }
    }

    @DynamoDbPartitionKey 
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<SentenceDyna> getSentences() {
		return sentences;
	}

	public void setSentences(List<SentenceDyna> sentences) {
		this.sentences = sentences;
	}
    
    
}
