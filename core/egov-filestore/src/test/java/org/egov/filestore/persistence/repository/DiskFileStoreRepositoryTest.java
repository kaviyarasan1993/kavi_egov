package org.egov.filestore.persistence.repository;


import org.egov.filestore.domain.model.Artifact;
import org.egov.filestore.domain.model.FileLocation;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DiskFileStoreRepositoryTest {

	@Mock
	private FileRepository fileRepository;

	private final String FILE_STORAGE_MOUNT_PATH = "some_path";
	private final String MODULE = "module_id";
	private final String TAG = "tag";
	private final String TENANT_ID = "tenantId";
	private DiskFileStoreRepository diskFileStoreRepository;

	@Before
	public void setup() {
		diskFileStoreRepository = new DiskFileStoreRepository(fileRepository, FILE_STORAGE_MOUNT_PATH);
	}

	@Test
	public void shouldStoreFileToDisk() throws Exception {
		MultipartFile file1 = mock(MultipartFile.class);
		MultipartFile file2 = mock(MultipartFile.class);
		String fileStoreId2 = UUID.randomUUID().toString();
		String fileStoreId1 = UUID.randomUUID().toString();
		List<Artifact> listOfMockedArtifacts = Arrays.asList(
				new Artifact(file1, new FileLocation(fileStoreId1, MODULE, TAG, TENANT_ID)),
				new Artifact(file2, new FileLocation(fileStoreId2, MODULE, TAG, TENANT_ID))
		);

		diskFileStoreRepository.write(listOfMockedArtifacts);

		verify(fileRepository).write(file1, Paths.get(FILE_STORAGE_MOUNT_PATH, TENANT_ID, MODULE, fileStoreId1));
		verify(fileRepository).write(file2, Paths.get(FILE_STORAGE_MOUNT_PATH, TENANT_ID, MODULE, fileStoreId2));
	}

	@Test
	public void shouldReturnResourceForGivenPath() {
		FileLocation fileLocation = new FileLocation("fileStoreId", MODULE, TAG, TENANT_ID);
		Resource expectedResource = mock(Resource.class);
		when(fileRepository.read(Paths.get(FILE_STORAGE_MOUNT_PATH, TENANT_ID, MODULE, "fileStoreId")))
				.thenReturn(expectedResource);

		Resource actualResource = diskFileStoreRepository.read(fileLocation);

		assertEquals(expectedResource, actualResource);

	}

}