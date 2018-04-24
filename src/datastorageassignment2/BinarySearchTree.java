/**
 * Ahmed Ali Eisawy 20110483
 * Mohamed Ramadan  20120309
 
 */
package datastorageassignment2;

import java.io.IOException;
import java.io.RandomAccessFile;

public class BinarySearchTree {

	private RandomAccessFile file;

	public void CreateRecordsFile(String filename, int numberOfRecords)
			throws IOException {
		file = new RandomAccessFile(filename, "rw");
		for (int i = 0; i < numberOfRecords - 1; i++) {
			file.writeInt(i + 1);
			file.writeInt(0);
			file.writeInt(0);
			file.writeInt(0);
		}
		file.writeInt(-1);
		file.writeInt(0);
		file.writeInt(0);
		file.writeInt(0);
		file.close();
	}

	public int InsertNewRecordAtIndex(String filename, int Key, int ByteOffset)
			throws IOException {
		int NextRecordEmpty = 0;
		file = new RandomAccessFile(filename, "rw");
		file.seek(0);
		NextRecordEmpty = file.readInt();
		if (NextRecordEmpty == -1) {
			return -1;
		} else {
			file.seek((NextRecordEmpty * 16));
			int UpdateNextRecordEmpty = file.readInt();
			file.seek(0);
			file.writeInt(UpdateNextRecordEmpty);
			file.seek((NextRecordEmpty * 16));
			file.writeInt(Key);
			file.writeInt(ByteOffset);
			file.writeInt(-1);
			file.writeInt(-1);

			// go to Root

			int searchtree = 1;
			while (true) {
				if (NextRecordEmpty == 1) {
					break;
				}
				file.seek(searchtree * 16);
				int value = file.readInt();
				file.readInt();
				int Left = file.readInt();
				int right = file.readInt();

				if ((Key < value) && (Left == -1)) {
					file.seek((searchtree * 16) + 8);
					file.writeInt(NextRecordEmpty);
					break;
				} else if ((Key < value) && (Left != -1)) {
					searchtree = Left;
				} else if (((Key > value) || (Key == value)) && (right == -1)) {
					file.seek((searchtree * 16) + 12);
					file.writeInt(NextRecordEmpty);
					break;
				} else if (((Key > value) || (Key == value)) && (right != -1)) {
					searchtree = right;
				}

			}

			/*
			 * file.seek(0); if ((file.length() / 16) < (NextRecordEmpty + 2)) {
			 * file.writeInt(-1); } else { file.seek(0);
			 * file.writeInt(UpdateNextRecordEmpty); }
			 */
		}
		return NextRecordEmpty;
	}

	public int SearchRecordInIndex(String filename, int Key) throws IOException {
		file = new RandomAccessFile(filename, "rw");
		file.seek(16);
		while (file.getFilePointer() < file.length()) {
			int value = file.readInt();
			int Offset = file.readInt();
			if (Key == value) {
				return Offset;
			}
			int Left = file.readInt();
			int right = file.readInt();
			if (Left == -1 && right == -1) {
				return -1;
			} else if (Key < value && Left != -1) {
				file.seek(Left * 16);
			} else if (Key > value && right != -1) {
				file.seek(right * 16);
			} else {
				return -1;
			}
		}
		return -1;
	}

	public void DisplayBinarySearchTreeInOrder(String filename)
			throws IOException {
		file = new RandomAccessFile(filename, "rw");
		System.out.println("\nBinary Search Tree In Order : \n");
		System.out.println("*********************************");
		System.out.println("* Key   | Ofset | Left  | Right *");
		display(file, 1);
		System.out.println("*********************************");
		file.close();
	}

	public void display(RandomAccessFile file, int Pointer) throws IOException {
		if (Pointer == -1) {
			return;
		}
		file.seek(Pointer * 16);
		int key = file.readInt();
		int ofset = file.readInt();
		int left = file.readInt();
		int right = file.readInt();
		display(file, left);
		System.out.println("********|*******|*******|********");
		System.out.println("* " + key + "  \t| " + ofset + " \t| " + left + " \t| "+ right + "\t*");
		display(file, right);
	}

	public void DisplayIndexFileContent(String filename) throws IOException {
		file = new RandomAccessFile(filename, "rw");
		System.out.println("\nIndex File Content\n");
		System.out.println("*********************************");
		System.out.println("* Key   | Ofset | Left  | Right *");

		while (file.getFilePointer() != file.length()) {
			int x = file.readInt();
			System.out.println("********|*******|*******|********");
			System.out.print("* " + x + "  \t| ");
			x = file.readInt();
			System.out.print(x + " \t| ");
			x = file.readInt();
			System.out.print(x + " \t| ");
			x = file.readInt();
			System.out.print(x + "\t*");
			System.out.println();
		}
		System.out.println("*********************************");
		file.close();

	}

	public static void main(String[] args) throws IOException {
		String FileName = "FileGenerated.txt";

		BinarySearchTree Object = new BinarySearchTree();
		Object.CreateRecordsFile(FileName, 8);
		Object.DisplayIndexFileContent(FileName);
		Object.InsertNewRecordAtIndex(FileName, 5, 12);
		Object.InsertNewRecordAtIndex(FileName, 12, 24);
		Object.InsertNewRecordAtIndex(FileName, 3, 36);
		Object.InsertNewRecordAtIndex(FileName, 9, 48);
		Object.InsertNewRecordAtIndex(FileName, 8, 60);
		Object.InsertNewRecordAtIndex(FileName, 2, 72);
		Object.InsertNewRecordAtIndex(FileName, 4, 84);
		Object.DisplayBinarySearchTreeInOrder(FileName);
		Object.DisplayIndexFileContent(FileName);
		System.out.println(Object.SearchRecordInIndex(FileName, 5));

	}

}
