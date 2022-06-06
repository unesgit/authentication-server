package ma.cam.kernal.dto.result;

import java.util.List;

import ma.cam.kernal.utils.Util;

public class PaginationResult<T> {
	private Long rowsNumber;
	
	private T results;

    public PaginationResult(T results) {
        
    	this.results = results;
        
        if(!Util.isNull(results) && !((List<?>)results).isEmpty()) {
        	rowsNumber = ((CommunResult) ((List<?>)results).get(0)).getRowsNumber();
        }else {
        	rowsNumber = 0l;
        }
    }

    
	public Object getResults() {
		return results;
	}

	public void setResults(T results) {
		this.results = results;
	}


	public Long getRowsNumber() {
		return rowsNumber;
	}


	public void setRowsNumber(Long rowsNumber) {
		this.rowsNumber = rowsNumber;
	}

  
}
