package lotto;

import lotto.util.LottoNumberRange;
import lotto.util.StringUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class UtilTest {
    @Test
    @DisplayName("로또 범위 static list")
    void lottoNumberRangeTest(){
        List<Integer> lottoNumberRange = LottoNumberRange.getLottoNumberRange();
        List<Integer> lottoNumberRange1 = LottoNumberRange.getLottoNumberRange();
        assertThat(lottoNumberRange).isEqualTo(lottoNumberRange1);
        assertThat(lottoNumberRange.size()).isEqualTo(45);
    }

    @Test
    @DisplayName("당첨 번호 입력에 따른 list 생성")
    void separatorTest(){
        String inputString = "1,2,3,4,5,6";
        String[] inputStringArray = StringUtil.separator(inputString);
        List<Integer> lottoNumbers = StringUtil.stringArrayToIntegerList(inputStringArray);
        assertThat(lottoNumbers).contains(1,2,3,4,5,6);
    }

    @Test
    @DisplayName("당첨 로또 중복 번호 확인")
    void dupleInputNumber(){
        String inputString = "1,1,1,1,1,1";
        assertThatThrownBy(() -> StringUtil.stringArrayToIntegerList(StringUtil.separator(inputString)))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("당첨 로또 6개 아닐 경우 확인")
    void notMaxCountInputNumber(){
        String inputString = "1,1,1,1,1";
        assertThatThrownBy(() -> StringUtil.stringArrayToIntegerList(StringUtil.separator(inputString)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}