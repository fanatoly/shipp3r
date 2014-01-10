package io.github.fanatoly.shipp3r

import java.nio.file._
import scala.collection.mutable.Buffer

object DirectoryScanner{
  val cnt = FileVisitResult.CONTINUE
  def listFiles(localPath: Path): List[Path] = {
    val result = Buffer[Path]()
    Files.walkFileTree(localPath, new FileVisitor[Path]{
      def postVisitDirectory(x: Path,y: java.io.IOException): FileVisitResult = cnt
      def preVisitDirectory(x: Path,y: attribute.BasicFileAttributes): FileVisitResult = cnt
      def visitFile(p: Path,attrs: attribute.BasicFileAttributes): FileVisitResult = {
        if(attrs.isRegularFile) result.append(p)
        cnt
      }
      def visitFileFailed(x: Path,y: java.io.IOException): FileVisitResult = cnt
    })
    result.toList
  }
}

case class S3Addr(bucketName: String, key: String)
case class S3Upload(local: Path, s3Path: S3Addr)

object S3FileMapper{
  def mapPaths(base: Path, s3base: String, paths: List[Path]): Set[S3Upload] = {
    val baseAddr = path2Addr(s3base)
    paths.map{ path =>
      S3Upload(path, baseAddr.copy(key = baseAddr.key + base.relativize(path).toString))
    }.toSet
  }

  def path2Addr(p: String): S3Addr = {
    val path =  ensureTrailingSlash(p)
    val slashIdx = path.indexOf('/')
    S3Addr(path.substring(0, slashIdx), path.substring(slashIdx + 1, path.length))
  }

  private def ensureTrailingSlash(str: String): String = {
    if(str.isEmpty) throw new RuntimeException("empty S3 path")
    else if(str(str.length - 1) == '/') return str
    else str + "/"
  }
}
