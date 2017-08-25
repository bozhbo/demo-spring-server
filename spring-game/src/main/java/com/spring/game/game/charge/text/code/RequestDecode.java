package com.snail.webgame.game.charge.text.code;

import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.snail.webgame.game.charge.text.util.TextUtil;

/**
 * 
 * 类介绍:消息解码类
 *
 * @author zhoubo
 * @2014-11-19
 */
public class RequestDecode extends CumulativeProtocolDecoder {

	private static Logger logger = LoggerFactory.getLogger("room");

	private static CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();

	public RequestDecode() {
	}

	protected boolean doDecode(IoSession session, ByteBuffer in, ProtocolDecoderOutput out) throws Exception {
		try {
			in.order(ByteOrder.LITTLE_ENDIAN);
			List<Object> list = readBuffer(in);

			if (list == null) {
				return false;
			} else {
				out.write(list);
				return true;
			}
		} catch (Exception e) {
			logger.error("Analyse data stream failure!", e);
			session.close();
			return false;
		}
	}

	/**
	 * 读取一个消息
	 * 
	 * @param in	输入缓冲
	 * @return	List<Object> 输出参数
	 */
	private List<Object> readBuffer(ByteBuffer in) {
		List<Object> valueList = null;
		int oldPostion = in.position();
		int endPostion = -1;

		if (in.remaining() > 2) {
			// 从ByteBuffer中读取一个完整消息
			for (int i = oldPostion; i < in.limit(); i++) {
				byte curChar = in.get(i);

				if (curChar == TextUtil.endR && (i + 1) < in.limit()) {
					if (in.get(i + 1) == TextUtil.endN) {
						endPostion = i + 2;
						break;
					}
				}
			}

			if (endPostion != -1) {
				// 从一个消息中读取空格分割的各个参数
				boolean isString = false;
				List<Byte> list = new ArrayList<Byte>();
				valueList = new ArrayList<Object>();

				for (int i = oldPostion; i < endPostion; i++) {
					byte c = in.get();
					
					try {
						if (i + 2 < endPostion) {
							// \r\n前的消息体
							if (c == TextUtil.string) {
								isString = true;
							} else if (c == TextUtil.block) {
								// 一个参数结束，解析此参数
								if (isString) {
									valueList.add(getString(list));
								} else {
									valueList.add(geInt(list));
								}

								isString = false;
								list.clear();
							} else {
								list.add(c);
							}
						}
					} catch (Exception e) {
						logger.error("translate data failure!", e);
					}
				}

				if (list.size() > 0) {
					if (isString) {
						valueList.add(getString(list));
					} else {
						valueList.add(geInt(list));
					}
				}

				return valueList;
			} else {
				return valueList;
			}
		}

		return valueList;
	}

	/**
	 * 通过byte数组取得String参数
	 * 
	 * @param list	byte数组
	 * @return	String	参数
	 */
	private static String getString(List<Byte> list) {
		StringBuffer buffer = new StringBuffer();
		
		if (list != null && list.size() > 0) {
			Byte b = list.get(0); // 前一个字符，用于判断是否是新字符的开始
			List<Byte> subList = new ArrayList<Byte>(); // 用于存储一个参数的临时集合
			subList.add(b);
			
			for (int i = 1; i < list.size(); i++) {
				byte c = list.get(i);
				
				if (TextUtil.isAsc(b) && TextUtil.isAsc(c)) {
					// 相同类型，继续添加
					subList.add(c);
				} else {
					if (b == TextUtil.gbkPrefix1 && c == TextUtil.gbkPrefix2) {
						int value = hex_to_char(list.get(i + 1).byteValue(), list.get(i + 2).byteValue());
						
						if (value <= 127 && value >= 0) {
							i += 3;
							
							buffer.append((char)value);
							
							subList.clear();
							
							if (i <= list.size() - 1) {
								c = list.get(i);
								subList.add(c);
							}
						} else {
							byte fitstByte = (byte)Integer.parseInt(
									String.valueOf((char)list.get(i + 1).byteValue()) + 
									String.valueOf((char)list.get(i + 2).byteValue()), 16);
							byte secondByte = (byte)Integer.parseInt(
									String.valueOf((char)list.get(i + 5).byteValue()) + 
									String.valueOf((char)list.get(i + 6).byteValue()), 16);
							byte thirdByte = (byte)Integer.parseInt(
									String.valueOf((char)list.get(i + 9).byteValue()) + 
									String.valueOf((char)list.get(i + 10).byteValue()), 16);
							buffer.append(new String(new byte[]{fitstByte, secondByte, thirdByte}, encoder.charset()));
							
							i += 11;
							
							subList.clear();
							
							if (i <= list.size() - 1) {
								c = list.get(i);
								subList.add(c);
							}
						}
					} else {
						char[] temp = new char[subList.size()];

						for (int j = 0; j < subList.size(); j++) {
							temp[j] = (char) subList.get(j).byteValue();
						}
						
						buffer.append(new String(temp));
						
						subList.clear();
					}
				}
				
				b = c;
			}
			
			if (subList.size() > 0) {
				if (TextUtil.isAsc(b)) {
					char[] temp = new char[subList.size()];

					for (int j = 0; j < subList.size(); j++) {
						temp[j] = (char) subList.get(j).byteValue();
					}
					
					buffer.append(new String(temp));
				} else {
					byte[] temp = new byte[subList.size()];

					for (int j = 0; j < subList.size(); j++) {
						temp[j] = subList.get(j).byteValue();
					}
					
					buffer.append(new String(temp, encoder.charset()));
				}
			}
		}
		
		return buffer.toString();
	}
	
	/**
	 * 通过byte数组取得intg参数
	 * 
	 * @param list	byte数组
	 * @return	Integer	参数
	 */
	private static Integer geInt(List<Byte> list) {
		if (list != null && list.size() > 0) {
			char[] temp = new char[list.size()];

			for (int j = 0; j < temp.length; j++) {
				temp[j] = (char)list.get(j).byteValue();
			}

			return Integer.valueOf(new String(temp));
		} else {
			logger.error("translate data to int error!");
			return -1;
		}
	}
	
	public static int hex_to_char(byte b1, byte b2) {
		int v = 0;
		byte[] b = { b1, b2 };

		for (int i = 0; i < 2; i++) {
			v <<= 4;

			char c = (char) b[i];

			if ((c >= '0') && (c <= '9')) {
				v += (int) (c - '0');
			} else if ((c >= 'A') && (c <= 'F')) {
				v += (int) (c - 'A' + 10);
			} else if ((c >= 'a') && (c <= 'f')) {
				v += (int) (c - 'a' + 10);
			} else {
				return 0;
			}
		}

		return v;
	}
	
//	public static void main(String[] args) {
//	ByteBuffer in = TextUtil.sendMessage("sb国家rs国rt", "head", "body", 100);
//
//	RequestDecode decode = new RequestDecode();
//	decode.readBuffer(in);
//}
}
