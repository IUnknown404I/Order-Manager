package mngr.orders_functional;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * A Visitor which copies directories with attachments to a new path
 * @author MrUnknown404
 */
public class DirectoryCopyVisitor extends SimpleFileVisitor<Path> {
    private Path fromPath;
    private Path toPath;
    private StandardCopyOption copyOption = StandardCopyOption.REPLACE_EXISTING;
    
    /**
     * We use it to run through all the directories and files to move them
     * @param fromPath The path to the copied content
     * @param toPath  The path to the replacement location 
     */
    public DirectoryCopyVisitor(Path fromPath, Path toPath){
        this.fromPath = fromPath;
        this.toPath = toPath;
    }
    
    @Override
    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
        Path targetPath = toPath.resolve(fromPath.relativize(dir));
        if(!Files.exists(targetPath)){
            Files.createDirectory(targetPath);
        }
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
        Files.copy(file, toPath.resolve(fromPath.relativize(file)), copyOption);
        
        return FileVisitResult.CONTINUE;
    }
    @Override
    public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
        return FileVisitResult.CONTINUE;
    }
    
    /**
     * Getter for new Path
     * @return new Path
     */
    public Path getToPath() {
        return toPath;
    }
    /**
     * Getter for old Path
     * @return the old Path
     */
    public Path getFromPath() {
        return fromPath;
    }
    /**
     * Getter for StandardCopyOption
     * @return current copy option
     */
    public StandardCopyOption getCopyOption() {
        return copyOption;
    }
}
