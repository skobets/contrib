package pt.utl.ist.repox.statistics;

import java.util.Date;
import java.util.Map;

public class RepoxStatistics {
	private Date generationDate;

	private int dataSourcesIdExtracted;
	private int dataSourcesIdGenerated;
	private int dataSourcesIdProvided;

    private int aggregators;
	private int dataProviders;
	private int dataSourcesOai;
	private int dataSourcesDirectoryImporter;
	
	//TODO: same as before, but for records with Digital Objects
	//TODO: Data Providers / by protocol (when z39.50 is ready)
	
	private Map<String, Integer> dataSourcesScheduled; // "daily" | "weekly" | "monthly" -> #
	private Map<String, Map<String, Integer>> harvestStatesPerPeriod; //State of harvest -> [period ("daily", ...) -> #]
	private Map<String, Integer> harvestStatesLength; //State of harvest -> length (s)
	

	private Map<String, Integer> dataSourcesMetadataFormats; // Map of format -> number of DataSources
	
	private float recordsAvgDataSource;
	private float recordsAvgDataProvider;
	private Map<String, Integer> countriesRecords;
	private int recordsTotal;
	
	public Date getGenerationDate() {
		return generationDate;
	}

	public void setGenerationDate(Date generationDate) {
		this.generationDate = generationDate;
	}

	public int getDataSourcesIdExtracted() {
		return dataSourcesIdExtracted;
	}

	public void setDataSourcesIdExtracted(int dataSourcesIdExtracted) {
		this.dataSourcesIdExtracted = dataSourcesIdExtracted;
	}

	public int getDataSourcesIdGenerated() {
		return dataSourcesIdGenerated;
	}

	public void setDataSourcesIdGenerated(int dataSourcesIdGenerated) {
		this.dataSourcesIdGenerated = dataSourcesIdGenerated;
	}

	public int getDataSourcesIdProvided() {
		return dataSourcesIdProvided;
	}

	public void setDataSourcesIdProvided(int dataSourcesIdProvided) {
		this.dataSourcesIdProvided = dataSourcesIdProvided;
	}

	public int getDataProviders() {
		return dataProviders;
	}

	public void setDataProviders(int dataProviders) {
		this.dataProviders = dataProviders;
	}

	public int getDataSourcesOai() {
		return dataSourcesOai;
	}

	public void setDataSourcesOai(int dataSourcesOai) {
		this.dataSourcesOai = dataSourcesOai;
	}

	public int getDataSourcesDirectoryImporter() {
		return dataSourcesDirectoryImporter;
	}

	public void setDataSourcesDirectoryImporter(int dataSourcesDirectoryImporter) {
		this.dataSourcesDirectoryImporter = dataSourcesDirectoryImporter;
	}

	public Map<String, Integer> getDataSourcesMetadataFormats() {
		return dataSourcesMetadataFormats;
	}

	public void setDataSourcesMetadataFormats(Map<String, Integer> dataSourcesMetadataFormats) {
		this.dataSourcesMetadataFormats = dataSourcesMetadataFormats;
	}

	public float getRecordsAvgDataSource() {
		return recordsAvgDataSource;
	}

	public void setRecordsAvgDataSource(float recordsAvgDataSource) {
		this.recordsAvgDataSource = recordsAvgDataSource;
	}

	public float getRecordsAvgDataProvider() {
		return recordsAvgDataProvider;
	}

	public void setRecordsAvgDataProvider(float recordsAvgDataProvider) {
		this.recordsAvgDataProvider = recordsAvgDataProvider;
	}

	public Map<String, Integer> getCountriesRecords() {
		return countriesRecords;
	}

	public void setCountriesRecords(Map<String, Integer> countriesRecords) {
		this.countriesRecords = countriesRecords;
	}

	public int getRecordsTotal() {
		return recordsTotal;
	}

	public void setRecordsTotal(int recordsTotal) {
		this.recordsTotal = recordsTotal;
	}

    public int getAggregators() {
        return aggregators;
    }

    public void setAggregators(int aggregators) {
        this.aggregators = aggregators;
    }

    public RepoxStatistics() {
		super();
		generationDate = new Date();
	}

	public RepoxStatistics(int dataSourcesIdExtracted, int dataSourcesIdGenerated, int dataSourcesIdProvided,
			int aggregators, int dataProviders, int dataSourcesOai, int dataSourcesDirectoryImporter,
			Map<String, Integer> dataSourcesMetadataFormats, float recordsAvgDataSource, float recordsAvgDataProvider,
			Map<String, Integer> countriesRecords, int recordsTotal) {
		this();
		this.dataSourcesIdExtracted = dataSourcesIdExtracted;
		this.dataSourcesIdGenerated = dataSourcesIdGenerated;
		this.dataSourcesIdProvided = dataSourcesIdProvided;
        this.aggregators = aggregators;
		this.dataProviders = dataProviders;
		this.dataSourcesOai = dataSourcesOai;
		this.dataSourcesDirectoryImporter = dataSourcesDirectoryImporter;
		this.dataSourcesMetadataFormats = dataSourcesMetadataFormats;
		this.recordsAvgDataSource = recordsAvgDataSource;
		this.recordsAvgDataProvider = recordsAvgDataProvider;
		this.countriesRecords = countriesRecords;
		this.recordsTotal = recordsTotal;
	}



}
