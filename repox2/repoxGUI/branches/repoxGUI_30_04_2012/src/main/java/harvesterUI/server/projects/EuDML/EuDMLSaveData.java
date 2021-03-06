//package harvesterUI.server.projects.EuDML;
//
//import harvesterUI.server.dataManagement.dataSets.Z39FileUpload;
//import harvesterUI.server.util.PagingUtil;
//import harvesterUI.server.util.Util;
//import harvesterUI.shared.*;
//import harvesterUI.shared.dataTypes.DataProviderUI;
//import harvesterUI.shared.dataTypes.DataSourceUI;
//import harvesterUI.shared.dataTypes.SaveDataResponse;
//import harvesterUI.shared.externalServices.ExternalServiceUI;
//import harvesterUI.shared.externalServices.ServiceParameterUI;
//import harvesterUI.shared.servletResponseStates.ResponseState;
//import org.apache.log4j.Logger;
//import org.dom4j.DocumentException;
//import pt.utl.ist.repox.RepoxManagerEuDml;
//import pt.utl.ist.repox.dataProvider.*;
//import pt.utl.ist.repox.dataProvider.dataSource.IdGenerated;
//import pt.utl.ist.repox.externalServices.ExternalRestService;
//import pt.utl.ist.repox.externalServices.ExternalServiceStates;
//import pt.utl.ist.repox.externalServices.ServiceParameter;
//import pt.utl.ist.repox.metadataTransformation.MetadataTransformation;
//import pt.utl.ist.repox.metadataTransformation.MetadataTransformationManager;
//import pt.utl.ist.repox.oai.DataSourceOai;
//import pt.utl.ist.repox.util.ConfigSingleton;
//import pt.utl.ist.util.FileUtil;
//import pt.utl.ist.util.exceptions.AlreadyExistsException;
//import pt.utl.ist.util.exceptions.IncompatibleInstanceException;
//import pt.utl.ist.util.exceptions.InvalidArgumentsException;
//import pt.utl.ist.util.exceptions.ObjectNotFoundException;
//
//import java.io.File;
//import java.io.IOException;
//import java.sql.SQLException;
//import java.text.Format;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.*;
//
///**
// * Created to REPOX.
// * User: Edmundo
// * Date: 04-07-2011
// * Time: 13:36
// */
//public class EuDMLSaveData {
//    private static final Logger log = Logger.getLogger(EuDMLSaveData.class);
//
//    public static SaveDataResponse saveDataProvider(boolean update,DataProviderUI dataProviderUI, int pageSize) {
//        String dpId;
//        SaveDataResponse saveDataResponse = new SaveDataResponse();
//        if(update) {
//            DataProvider dataProvider = ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().getDataProvider(dataProviderUI.getId());
//            if(dataProvider != null) {
//                try {
//                    dataProvider = ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().updateDataProvider(dataProvider.getId(),
//                            dataProviderUI.getName(), dataProviderUI.getCountry(), dataProviderUI.getDescription());
//                    dpId = dataProvider.getId();
//                    saveDataResponse.setPage(PagingUtil.getDataPage(dpId, pageSize));
//                    saveDataResponse.setResponseState(ResponseState.SUCCESS);
//                } catch (ObjectNotFoundException e) {
//                    saveDataResponse.setResponseState(ResponseState.NOT_FOUND);
//                } catch (IOException e) {
//                    saveDataResponse.setResponseState(ResponseState.OTHER);
//                }
//            }
//        } else {
//            try {
//                DataProvider dataProvider = ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().createDataProvider(dataProviderUI.getName(),
//                        dataProviderUI.getCountry(), dataProviderUI.getDescription());
//                saveDataResponse.setPage(PagingUtil.getDataPage(dataProvider.getId(),pageSize));
//                saveDataResponse.setResponseState(ResponseState.SUCCESS);
//            } catch (IOException e) {
//                saveDataResponse.setResponseState(ResponseState.OTHER);
//            } catch (AlreadyExistsException e) {
//                saveDataResponse.setResponseState(ResponseState.ALREADY_EXISTS);
//            }
//        }
//        return saveDataResponse;
//    }
//
//    public static String deleteDataProviders(List<DataProviderUI> dataProviderUIs) {
//        for (DataProviderUI dataProvider : dataProviderUIs) {
//            try {
//                ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().deleteDataProvider(dataProvider.getId());
//            } catch (IOException e) {
//                return MessageType.OTHER.name();
//            } catch (ObjectNotFoundException e) {
//                return MessageType.NOT_FOUND.name();
//            }
//        }
//        System.out.println("Done dps removed");
//        return MessageType.OK.name();
//    }
//
//    // DATA SOURCES
//    public static SaveDataResponse saveDataSource(boolean update, String type, String originalDSset, DataSourceUI dataSourceUI, int pageSize) throws ServerSideException {
//        SaveDataResponse saveDataResponse = new SaveDataResponse();
//        try {
//            RepoxManagerEuDml repoxManagerEuDml = (RepoxManagerEuDml)ConfigSingleton.getRepoxContextUtil().getRepoxManager();
//
//            ResponseState urlStatus = Util.getUrlStatus(dataSourceUI);
//            if(urlStatus != null){
//                saveDataResponse.setResponseState(urlStatus);
//                return saveDataResponse;
//            }
//
//            // Save metadata transformations
//            MetadataTransformationManager metadataTransformationManager = ConfigSingleton.getRepoxContextUtil().getRepoxManager().getMetadataTransformationManager();
//            Map<String, MetadataTransformation> metadataTransformations = new HashMap<String, MetadataTransformation>();
//            for(TransformationUI transformationUI: dataSourceUI.getMetadataTransformations()) {
//                MetadataTransformation loadedTransformation = metadataTransformationManager.loadMetadataTransformation(transformationUI.getIdentifier());
//                metadataTransformations.put(transformationUI.getIdentifier(),loadedTransformation);
//            }
//
//            // Save external services
//            List<ExternalRestService> externalRestServices = saveExternalServices(dataSourceUI);
//
//            if(update) {
//                // Check if the id already exists
//                DataSourceContainer dataSourceContainer = repoxManagerEuDml.getDataManager().getDataSourceContainer(dataSourceUI.getDataSourceSet());
//                DataSourceContainer originalDSC = repoxManagerEuDml.getDataManager().getDataSourceContainer(originalDSset);
//                if(dataSourceContainer != null && !originalDSC.getDataSource().getId().equals(dataSourceUI.getDataSourceSet())){
//                    saveDataResponse.setResponseState(ResponseState.ALREADY_EXISTS);
//                    return saveDataResponse;
//                }
//
//                DataSource createdDataSource = null;
//                try{
//                    if(type.equals("oai")) {
//                        createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceOai(originalDSset, dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                dataSourceUI.getSourceMDFormat(), dataSourceUI.getOaiSource(),
//                                dataSourceUI.getOaiSet(), metadataTransformations,externalRestServices);
//                    } else if(type.equals("folder")) {
//                        if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.marc.DataSourceFolder")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceFolder(originalDSset, dataSourceUI.getDataSourceSet(),
//                                    dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getDirPath(), metadataTransformations,
//                                    externalRestServices);
//                        } else if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.ftp.DataSourceFtp")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            // Check FTP connection
//                            if(dataSourceUI.getUser() != null && !dataSourceUI.getUser().isEmpty()) {
//                                if(!FileUtil.checkFtpServer(dataSourceUI.getServer(), "Normal", dataSourceUI.getFolderPath(),
//                                        dataSourceUI.getUser(), dataSourceUI.getPassword())){
//                                    saveDataResponse.setResponseState(ResponseState.FTP_CONNECTION_FAILED);
//                                    return saveDataResponse;
//                                }
//                            }
//
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceFtp(originalDSset,
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getServer(),
//                                    dataSourceUI.getUser(), dataSourceUI.getPassword(), dataSourceUI.getFolderPath(),
//                                    metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.ftp.DataSourceHTTP")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceHttp(originalDSset,
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getHttpURL(),
//                                    metadataTransformations,externalRestServices);
//                        }
//                    } else if(type.equals("z39")) {
//                        // Harvest Method differences
//                        if(dataSourceUI.getZ39HarvestMethod().equals("IdSequenceHarvester")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceZ3950IdSequence(originalDSset,
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), dataSourceUI.getZ39MaximumId(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getZ39HarvestMethod().equals("IdListHarvester")) {
//                            // check z3950 file upload
//                            File z3950 = null;
//                            if(!Z39FileUpload.ignoreUploadFile()) {
//                                z3950 = Z39FileUpload.getZ39TempFile();
//                                Z39FileUpload.deleteTempFile();
//                            }
//
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceZ3950IdList(originalDSset,
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), z3950 != null ? z3950.getAbsolutePath() : "",
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getZ39HarvestMethod().equals("TimestampHarvester")) {
//                            Format formatter = new SimpleDateFormat("yyyyMMdd");
//                            String earliestDateString = formatter.format(dataSourceUI.getZ39EarlistDate());
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().updateDataSourceZ3950Timestamp(originalDSset,
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), earliestDateString,
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        }
//                    }
//                    // External Services Run Type
//                    if(dataSourceUI.getExternalServicesRunType() != null)
//                        createdDataSource.setExternalServicesRunType(
//                                ExternalServiceStates.ContainerType.valueOf(dataSourceUI.getExternalServicesRunType()));
//
//                    createdDataSource.setExportDir(dataSourceUI.getExportDirectory());
//                    saveDataResponse.setPage(PagingUtil.getDataPage(createdDataSource.getId(),pageSize));
//                    saveDataResponse.setResponseState(ResponseState.SUCCESS);
//                } catch (ParseException e) {
//                    saveDataResponse.setResponseState(ResponseState.OTHER);
//                } catch (ObjectNotFoundException e) {
//                    saveDataResponse.setResponseState(ResponseState.NOT_FOUND);
//                } catch (InvalidArgumentsException e) {
//                    saveDataResponse.setResponseState(ResponseState.INVALID_ARGUMENTS);
//                } catch (IncompatibleInstanceException e) {
//                    saveDataResponse.setResponseState(ResponseState.INCOMPATIBLE_TYPE);
//                }
//                return saveDataResponse;
//            }
//            else {
//                // New Data Source
//                // Check if the id already exists
//                DataSourceContainer dataSourceContainerTest = repoxManagerEuDml.getDataManager().getDataSourceContainer(dataSourceUI.getDataSourceSet());
//                if(dataSourceContainerTest != null){
//                    saveDataResponse.setResponseState(ResponseState.ALREADY_EXISTS);
//                    return saveDataResponse;
//                }
//
//                DataSource createdDataSource = null;
//                try {
//                    createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceOai(dataSourceUI.getDataSetParent().getId(),
//                            dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                            dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                            dataSourceUI.getSourceMDFormat(), dataSourceUI.getOaiSource(),
//                            dataSourceUI.getOaiSet(), metadataTransformations,externalRestServices);
//                    if(type.equals("oai")) {
//                    } else if(type.equals("folder")) {
//                        if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.marc.DataSourceFolder")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceFolder(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getDirPath(),
//                                    metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.ftp.DataSourceFtp")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            // Check FTP connection
//                            if(dataSourceUI.getUser() != null && !dataSourceUI.getUser().isEmpty()) {
//                                if(!FileUtil.checkFtpServer(dataSourceUI.getServer(), "Normal", dataSourceUI.getFolderPath(),
//                                        dataSourceUI.getUser(), dataSourceUI.getPassword())){
//                                    saveDataResponse.setResponseState(ResponseState.FTP_CONNECTION_FAILED);
//                                    return saveDataResponse;
//                                }
//                            }
//
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceFtp(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getServer(),
//                                    dataSourceUI.getUser(), dataSourceUI.getPassword(), dataSourceUI.getFolderPath(),
//                                    metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getRetrieveStartegy().equals("pt.utl.ist.repox.ftp.DataSourceHTTP")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceHttp(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(), dataSourceUI.getSourceMDFormat(),
//                                    dataSourceUI.getIsoVariant(), dataSourceUI.getCharacterEncoding(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, dataSourceUI.getRecordRootName(), dataSourceUI.getHttpURL(),
//                                    metadataTransformations,externalRestServices);
//                        }
//                    } else if(type.equals("z39")) {
//                        // Harvest Method differences
//                        if(dataSourceUI.getZ39HarvestMethod().equals("IdSequenceHarvester")) {
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceZ3950IdSequence(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), dataSourceUI.getZ39MaximumId(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getZ39HarvestMethod().equals("IdListHarvester")) {
//                            // check z3950 file upload
//                            File z3950 = null;
//                            if(!Z39FileUpload.ignoreUploadFile()) {
//                                z3950 = Z39FileUpload.getZ39TempFile();
//                                Z39FileUpload.deleteTempFile();
//                            }
//
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceZ3950IdList(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), z3950.getAbsolutePath(),
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        } else if(dataSourceUI.getZ39HarvestMethod().equals("TimestampHarvester")) {
//                            Format formatter = new SimpleDateFormat("yyyyMMdd");
//                            String earliestDateString = formatter.format(dataSourceUI.getZ39EarlistDate());
//                            Map<String, String> namespaces = new HashMap<String, String>();
//                            if(dataSourceUI.getRecordIdPolicy().equals("IdExtracted")) {
//                                for(int i=0; i<dataSourceUI.getNamespaceList().size(); i+=2) {
//                                    namespaces.put(dataSourceUI.getNamespaceList().get(i),
//                                            dataSourceUI.getNamespaceList().get(i+1));
//                                }
//                            }
//                            createdDataSource = repoxManagerEuDml.getDataManager().createDataSourceZ3950Timestamp(dataSourceUI.getDataSetParent().getId(),
//                                    dataSourceUI.getDataSourceSet(), dataSourceUI.getDescription(),
//                                    dataSourceUI.getSchema(), dataSourceUI.getMetadataNamespace(),
//                                    dataSourceUI.getZ39Address(), dataSourceUI.getZ39Port(), dataSourceUI.getZ39Database(),
//                                    dataSourceUI.getZ39User(), dataSourceUI.getZ39Password(), dataSourceUI.getZ39RecordSyntax(),
//                                    dataSourceUI.getCharacterEncoding(), earliestDateString,
//                                    dataSourceUI.getRecordIdPolicy(), dataSourceUI.getIdXPath(),
//                                    namespaces, metadataTransformations,externalRestServices);
//                        }
//                    }
//                } catch (SQLException e) {
//                    saveDataResponse.setResponseState(ResponseState.ERROR_DATABASE);
//                } catch (ObjectNotFoundException e) {
//                    saveDataResponse.setResponseState(ResponseState.NOT_FOUND);
//                } catch (AlreadyExistsException e) {
//                    saveDataResponse.setResponseState(ResponseState.ALREADY_EXISTS);
//                } catch (InvalidArgumentsException e) {
//                    saveDataResponse.setResponseState(ResponseState.INVALID_ARGUMENTS);
//                } catch (ParseException e) {
//                    saveDataResponse.setResponseState(ResponseState.OTHER);
//                }
//                // External Services Run Type
//                if(dataSourceUI.getExternalServicesRunType() != null)
//                    createdDataSource.setExternalServicesRunType(
//                            ExternalServiceStates.ContainerType.valueOf(dataSourceUI.getExternalServicesRunType()));
//
//                createdDataSource.setExportDir(dataSourceUI.getExportDirectory());
//
//                saveDataResponse.setPage(PagingUtil.getDataPage(createdDataSource.getId(),pageSize));
//                saveDataResponse.setResponseState(ResponseState.SUCCESS);
//                return saveDataResponse;
//            }
//        }catch (DocumentException e) {
//            saveDataResponse.setResponseState(ResponseState.OTHER);
//            return saveDataResponse;
//        } catch (IOException e) {
//            saveDataResponse.setResponseState(ResponseState.OTHER);
//            return saveDataResponse;
//        }
//    }
//
//
//    public static String deleteDataSources(List<DataSourceUI> dataSourceUIs) {
//        Iterator<DataSourceUI> dataSourceUIIterator = dataSourceUIs.iterator();
//        while (dataSourceUIIterator.hasNext()) {
//            try {
//                ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().deleteDataSourceContainer(dataSourceUIIterator.next().getDataSourceSet());
//            } catch (IOException e) {
//                e.printStackTrace();
//                log.error(e.getMessage());
//            }
//        }
//        System.out.println("Done dss removed");
//        return "success";
//    }
//
//    public static void addAllOAIURL(String url,String dataProviderID,String dsSchema,String dsNamespace,
//                                    String dsMTDFormat, Map<String,List<String>> map) {
//        try {
//            List<String> sets = map.get("sets");
//            List<String> setNames = map.get("setNames");
//
//            DataProvider dataProvider = ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().
//                    getDataProvider(dataProviderID);
//
//            for (int i=0; i<sets.size(); i++) {
//                String setSpec = sets.get(i);
//                String setDescription = setNames.get(i);
//
//                String setId = setSpec.replaceAll("[^a-zA-Z_0-9]", "_");
//
//                DataSourceOai dataSourceOai = new DataSourceOai(dataProvider, setId, setDescription,
//                        dsSchema, dsNamespace, dsMTDFormat,
//                        url, setSpec, new IdGenerated(), new TreeMap<String, MetadataTransformation>());
//
//                HashMap<String, DataSourceContainer> oldDataSourceContainers = dataProvider.getDataSourceContainers();
//
//                if(oldDataSourceContainers == null) {
//                    dataProvider.setDataSourceContainers(new HashMap<String, DataSourceContainer>());
//                }
//
//                boolean isDuplicate = false;
//                if (oldDataSourceContainers != null) {
//                    for (DataSourceContainer dataSourceContainer : oldDataSourceContainers.values()) {
//                        DataSource oldDataSource = dataSourceContainer.getDataSource();
//                        if (oldDataSource instanceof DataSourceOai
//                                && ((DataSourceOai) oldDataSource).isSameDataSource(dataSourceOai)) {
//                            isDuplicate = true;
//                        }
//                    }
//                }
//
//                if (!isDuplicate) {
//                    while (ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().getDataSourceContainer(dataSourceOai.getId()) != null) {
//                        dataSourceOai.setId(dataSourceOai.getId() + "_new");
//                    }
//                    DataSourceContainerDefault dataSourceContainer = new DataSourceContainerDefault(dataSourceOai);
//                    dataProvider.getDataSourceContainers().put(dataSourceOai.getId(),dataSourceContainer);
//
//                    dataSourceOai.initAccessPoints();
//                }
//            }
//            ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().updateDataProvider(dataProvider, dataProvider.getId());
//            ConfigSingleton.getRepoxContextUtil().getRepoxManager().getAccessPointsManager().initialize(dataProvider.getDataSourceContainers());
//        } catch (DocumentException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        } catch (ObjectNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        System.out.println("Done add all");
//    }
//
//    public static String getDirPathFtp(String dataSourceId){
//        return ConfigSingleton.getRepoxContextUtil().getRepoxManager().getDataManager().getDirPathFtp(dataSourceId);
//    }
//
//    public static List<ExternalRestService> saveExternalServices(DataSourceUI dataSourceUI){
//        List<ExternalRestService> externalRestServices = new ArrayList<ExternalRestService>();
//        // Rest Service Data
//        if(dataSourceUI.getRestServiceUIList().size() > 0) {
//            for(ExternalServiceUI externalServiceUI : dataSourceUI.getRestServiceUIList()){
//                ExternalRestService externalRestService = new ExternalRestService(externalServiceUI.getId(),
//                        externalServiceUI.getName(),externalServiceUI.getUri(),
//                        externalServiceUI.getStatusUri(),externalServiceUI.getType());
//                for(ServiceParameterUI serviceParameterUI : externalServiceUI.getServiceParameters()){
//                    ServiceParameter serviceParameter = new ServiceParameter(serviceParameterUI.getName(),
//                            serviceParameterUI.getType(),serviceParameterUI.getRequired(),serviceParameterUI.getExample(),
//                            serviceParameterUI.getSemantics());
//                    serviceParameter.setValue(serviceParameterUI.getValue());
//                    externalRestService.getServiceParameters().add(serviceParameter);
//                }
//                externalRestServices.add(externalRestService);
//            }
//        }
//
//        return externalRestServices;
//    }
//}
