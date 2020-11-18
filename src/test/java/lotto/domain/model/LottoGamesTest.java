package lotto.domain.model;

import lotto.domain.Lotto;
import lotto.domain.LottoResults;
import lotto.domain.PrizeGrade;
import lotto.domain.WinnerLotto;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static lotto.domain.PrizeGrade.*;
import static org.assertj.core.api.Assertions.assertThat;

class LottoGamesTest {

  @ParameterizedTest
  @MethodSource("provideLottoResultMaterial")
  void 로또_당첨결과_확인_테스트(LottoGames lottoGames, WinnerLotto winnerLotto, List<PrizeGrade> expect) {
    assertThat(lottoGames.confirmPrize(winnerLotto)).isEqualTo(new LottoResults(expect));
  }

  static Stream<Arguments> provideLottoResultMaterial() {

    String[] _1To6 = IntStream.range(1, 7).mapToObj(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});
    String[] _1To5And10 = IntStream.of(1, 2, 3, 4, 5, 10).mapToObj(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});
    String[] _1To5And9 = IntStream.of(1, 2, 3, 4, 5, 9).mapToObj(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});
    String[] _1To4And7_8 = IntStream.of(1, 2, 3, 4, 7, 8).mapToObj(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});
    String[] _20To4And25 = IntStream.range(20, 26).mapToObj(String::valueOf).collect(Collectors.toList()).toArray(new String[]{});

    LottoNumber bonusNumber = new LottoNumber(10);
    return Stream.of(
        Arguments.of(
            new LottoGames(Arrays.asList(
                new Lotto(_1To6),
                new Lotto(_1To5And10),
                new Lotto(_1To5And9),
                new Lotto(_1To4And7_8),
                new Lotto(_20To4And25)
            )),
            new WinnerLotto(new Lotto(_1To6), bonusNumber),
            Arrays.asList(FIRST_CLASS, SECOND_CLASS, THIRD_CLASS, FOURTH_CLASS, FAIL)
        )
    );
  }


}