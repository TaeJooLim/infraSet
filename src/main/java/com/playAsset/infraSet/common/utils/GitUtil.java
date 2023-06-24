package com.playAsset.infraSet.common.utils;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.jgit.api.CommitCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.PullCommand;
import org.eclipse.jgit.api.PullResult;
import org.eclipse.jgit.api.PushCommand;
import org.eclipse.jgit.api.errors.AbortedByHookException;
import org.eclipse.jgit.api.errors.CanceledException;
import org.eclipse.jgit.api.errors.ConcurrentRefUpdateException;
import org.eclipse.jgit.api.errors.DetachedHeadException;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidConfigurationException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.NoFilepatternException;
import org.eclipse.jgit.api.errors.NoHeadException;
import org.eclipse.jgit.api.errors.NoMessageException;
import org.eclipse.jgit.api.errors.RefNotAdvertisedException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;
import org.eclipse.jgit.api.errors.UnmergedPathsException;
import org.eclipse.jgit.api.errors.WrongRepositoryStateException;
import org.eclipse.jgit.lib.IndexDiff;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.eclipse.jgit.treewalk.FileTreeIterator;
import org.eclipse.jgit.util.FS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitUtil {

    private static Logger logger = LoggerFactory.getLogger(GitUtil.class);
    
    /**
     * .git폴더를 찾아 Git의 정보를 리턴한다.
     * @param path .git 폴더가 위치한 경로
     * @return  Git Git정보
     * @throws Exception
     */
    public static Git openGit(String path) throws Exception {
        return openGit(new File(path));
    }

    /**
     * .git폴더를 찾아 Git의 정보를 리턴한다.
     * @param path .git 폴더가 위치한 경로
     * @return  Git Git정보
     * @throws Exception
     */
    private static Git openGit(File file) throws Exception {
        Git git = null;
        try {
            git = Git.open(file, FS.detect(true));
            return git;
        } catch (Throwable th) {
            throw new Exception(th.getMessage(), th);
        } finally {
            if (git != null)
                git.close();
        }
    }

    /**
     * Git Repository로부터 Pull을 실행해 형상을 받아온다.
     * @param git 깃정보
     * @param userId 깃아이디
     * @param passwd 깃비밀번호
     * @return
     * @throws WrongRepositoryStateException
     * @throws InvalidConfigurationException
     * @throws DetachedHeadException
     * @throws InvalidRemoteException
     * @throws CanceledException
     * @throws RefNotFoundException
     * @throws RefNotAdvertisedException
     * @throws TransportException
     * @throws GitAPIException
     */
    public static boolean pullFromRepository(Git git, String userId, String passwd) 
        throws  WrongRepositoryStateException, InvalidConfigurationException, DetachedHeadException, 
                InvalidRemoteException, CanceledException, RefNotFoundException, 
                RefNotAdvertisedException, TransportException, GitAPIException {

        PullCommand pullCmd = git.pull();
        logger.info("Git Pull From Repository START");

        pullCmd.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userId, passwd));
        logger.info("Git Credentials Provide SUCCESS");

        PullResult pullResult = pullCmd.call();
        logger.info("Git Pull SUCCESS");
        return pullResult.isSuccessful();
    }

    /**
     * LocalRepository에 커밋메시지와 Commit을 실행한 사람의 정보를 Commit한다.
     * @param git 깃정보
     * @param commitMsg 커밋메시지
     * @param userId 깃아이디
     * @param userEmail 깃이메일
     * @throws NoHeadException
     * @throws NoMessageException
     * @throws UnmergedPathsException
     * @throws ConcurrentRefUpdateException
     * @throws WrongRepositoryStateException
     * @throws AbortedByHookException
     * @throws GitAPIException
     */
    public static void commitLocalRepository(Git git, String commitMsg, String userId, String userEmail) 
        throws  NoHeadException, NoMessageException, UnmergedPathsException, 
                ConcurrentRefUpdateException, WrongRepositoryStateException, AbortedByHookException, 
                GitAPIException {

        CommitCommand commitCmd = git.commit();
        commitCmd.setMessage(commitMsg).setAuthor(userId, userEmail);
        commitCmd.call();
    }

    /**
     * 형상을 Git Remote Repository에 push한다.
     * @param git 깃정보
     * @param userId 깃아이디
     * @param passwd 깃비밀번호
     * @throws InvalidRemoteException
     * @throws TransportException
     * @throws GitAPIException
     */
    public static void pushRemoteRepository(Git git, String userId, String passwd)
        throws InvalidRemoteException, TransportException, GitAPIException {

        PushCommand pushCmd = git.push();
        pushCmd.setCredentialsProvider(new UsernamePasswordCredentialsProvider(userId, passwd));

        pushCmd.call();
    }

    /**
     * remote의 최신형상을 비교해서 바뀐 형상을 인덱싱한다.
     * @param git
     * @param path
     * @param isDelete
     * @throws NoFilepatternException
     * @throws GitAPIException
     */
    public static void addIndex(Git git, String path, boolean isDelete)
        throws NoFilepatternException, GitAPIException {

        if (isDelete)
            git.rm().addFilepattern(path).call();
        else 
            git.add().addFilepattern(path).call();
    }

    /**
     * 로컬에서 변경된 파일의 상태
     */
    public static enum FileStatus {
        ADDED, CHANGED, REMOVED, MODIFIED, UNTRACKED, MISSING, ASSUME_UNCHANGED, CONFLICTING
    }

    /**
     * remote의 최신형상과 로컬에서 변경된 파일을 비교 후 목록을 가져온다.
     * @throws IOException
     */
    public static Map<FileStatus, Set<String>> getIndexingList(Repository repository) throws IOException {
        Map<FileStatus, Set<String>> map = new LinkedHashMap<>();
        IndexDiff indexDiff = new IndexDiff(repository, "HEAD", new FileTreeIterator(repository));
        indexDiff.diff();

        map.put(FileStatus.ADDED, indexDiff.getAdded());
        map.put(FileStatus.CHANGED, indexDiff.getChanged());
        map.put(FileStatus.REMOVED, indexDiff.getRemoved());
        map.put(FileStatus.MODIFIED, indexDiff.getModified());
        map.put(FileStatus.UNTRACKED, indexDiff.getUntracked());
        map.put(FileStatus.MISSING, indexDiff.getMissing());
        map.put(FileStatus.ASSUME_UNCHANGED, indexDiff.getAssumeUnchanged());
        map.put(FileStatus.CONFLICTING, indexDiff.getConflicting());

        return map;
    } 
}
