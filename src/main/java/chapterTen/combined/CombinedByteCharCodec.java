package chapterTen.combined;

import chapterTen.decoder.ByteToCharDecoder;
import chapterTen.encoder.CharToByteEncoder;
import io.netty.channel.CombinedChannelDuplexHandler;

/**
 * @author renfakai
 * 代码清单10-10
 */
public class CombinedByteCharCodec extends CombinedChannelDuplexHandler<ByteToCharDecoder, CharToByteEncoder> {


    public CombinedByteCharCodec() {
        super(new ByteToCharDecoder(), new CharToByteEncoder());
    }
}
