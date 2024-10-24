![Logo](https://play-lh.googleusercontent.com/mD2x6kIY8JaKK55CsS6HbDn4hPAp1MslSxAhiIyhKxobpWxpmYoZakUlITmJkjqc1g=w240-h480)

# ForBlitz Statistics

ForBlitz Statistics — Android-приложение для просмотра внутриигровой статистики аккаунтов WoT/Tanks Blitz. 

ForBlitz Statistics позволяет:
- Сохранять сессионную статистику и легко сравнивать её с текущей
- Просматривать статистику аккаунта с любого из регионов WoT/Tanks Blitz
- Просматривать детальную статистику случайных боёв
- Просматривать детальную статистику рейтинговых боёв
- Просматривать детальную информацию о клане, включая условия авторекрутинга
- Просматривать детальную статистику техники

## Функциональные требования

- Просмотр боевой статистики аккаунта, расположенного на  любом игровом кластере (RU, EU, NA, ASIA)
- Боевая статистика игрока должна включать:
  - Статистику случайных боев
  - Статистику рейтинговых боев
  - Статистику всей и конкретной техники игрока
- Просмотр полной информации о клане игрока, такой как:
  - Название, девиз и описание клана
  - Никнейм создателя и дата создания
  - Дата переименования, старые название и тег клана, если таковые имеются
  - Условия авторекрутинга
  - Список участников клана с ролью и датой присоединения
- Возможность сохранения и последующего сравнения текущей статистики с разными датами, а также просмотра статистики за конкретный период между просмотрами статистики игрока.

## Нефункциональные требования

### Стабильность

Требуется минимизировать количество критических ошибок и неточностей в информации, включая те, что могут случиться по вине пользователя.

### Производительность

Основными мерками производительности являются скорость загрузки контента и частота кадров при обработке интерфейса приложения. Необходимо удерживать эти два параметра в комфортных пределах за счет оптимизации количества дублирующейся подгружаемой из сети информации и сложных графических элементов.

### Удобство использования

Требуется обеспечить удобное и интуитивное управление приложением, включая минимальное количество кликов для перехода из одного раздела интерфейса в любой другой.